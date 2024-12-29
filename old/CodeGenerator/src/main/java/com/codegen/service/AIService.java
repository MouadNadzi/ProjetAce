// codegen-service/src/main/java/com/example/codegen/service/AIService.java
package com.codegen.service;

import com.codegen.model.entity.ParsedUMLModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIService {

    @Value("${gemini_api_key:YOUR_API_KEY}")
    private String apiKey;

    /**
     * Sends the Base64 UML diagram to Gemini, gets structured JSON,
     * then returns a ParsedUMLModel.
     */
    public ParsedUMLModel parseDiagram(String base64Image) {
        // 1) Build request payload
        String requestBody = buildGeminiRequest(base64Image);

        // 2) Call Gemini
        String geminiEndpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(geminiEndpoint, requestBody, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // 3) Extract text from Gemini JSON
            String text = extractTextFromGemini(response.getBody());
            // 4) Convert that text (which should be JSON) into ParsedUMLModel
            return parseUMLJson(text);
        } else {
            throw new RuntimeException("Gemini call failed: " + response.getStatusCode() + " / " + response.getBody());
        }
    }

    private String buildGeminiRequest(String base64Image) {
        // Minimal prompt
        String promptText = """
        Analyze this UML class diagram and return JSON in the form:
        {
          "classes": [
            {
              "name": "MyClass",
              "fields": [ { "name":"fieldName", "type":"String" } ],
              "relationships": [ { "type":"OneToMany", "target":"AnotherClass" } ]
            }
          ]
        }
        """;

        // Build JSON
        JSONObject root = new JSONObject();
        JSONArray contentsArray = new JSONArray();
        JSONObject contentObj = new JSONObject();
        JSONArray partsArray = new JSONArray();

        // Part 1: text prompt
        JSONObject promptPart = new JSONObject();
        promptPart.put("text", promptText);
        partsArray.add(promptPart);

        // Part 2: inline data with Base64
        JSONObject inlinePart = new JSONObject();
        JSONObject inlineData = new JSONObject();
        inlineData.put("mime_type", "image/jpeg");
        inlineData.put("data", base64Image);
        inlinePart.put("inline_data", inlineData);
        partsArray.add(inlinePart);

        contentObj.put("parts", partsArray);
        contentsArray.add(contentObj);
        root.put("contents", contentsArray);

        // Optionally add generationConfig, safetySettings, etc.
        return root.toJSONString();
    }

    /**
     * Extract text from Gemini's JSON response:
     * Typically looking in `candidates[0].content.parts[0].text`.
     */
    private String extractTextFromGemini(String geminiResponse) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(geminiResponse);
            JSONArray candidates = (JSONArray) root.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                JSONObject firstCandidate = (JSONObject) candidates.get(0);
                JSONObject contentObj = (JSONObject) firstCandidate.get("content");
                JSONArray parts = (JSONArray) contentObj.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    JSONObject firstPart = (JSONObject) parts.get(0);
                    return (String) firstPart.get("text");
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse Gemini response JSON: " + e.getMessage());
        }
        return "";
    }

    /**
     * Convert the AI's JSON string into a ParsedUMLModel.
     */
    private ParsedUMLModel parseUMLJson(String jsonText) {
        ParsedUMLModel model = new ParsedUMLModel();
        try {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(jsonText);
            JSONArray classesArray = (JSONArray) root.get("classes");
            if (classesArray != null) {
                List<ParsedUMLModel.UMLClass> umlClasses = new ArrayList<>();
                for (Object obj : classesArray) {
                    JSONObject cls = (JSONObject) obj;
                    ParsedUMLModel.UMLClass umlClass = new ParsedUMLModel.UMLClass();
                    umlClass.setName((String) cls.get("name"));

                    // Fields
                    JSONArray fieldsArray = (JSONArray) cls.get("fields");
                    if (fieldsArray != null) {
                        List<ParsedUMLModel.UMLField> fieldList = new ArrayList<>();
                        for (Object fieldObj : fieldsArray) {
                            JSONObject f = (JSONObject) fieldObj;
                            ParsedUMLModel.UMLField field = new ParsedUMLModel.UMLField(
                                    (String) f.get("name"),
                                    (String) f.get("type")
                            );
                            fieldList.add(field);
                        }
                        umlClass.setFields(fieldList);
                    }

                    // Relationships
                    JSONArray relArray = (JSONArray) cls.get("relationships");
                    if (relArray != null) {
                        List<ParsedUMLModel.UMLRelationship> relList = new ArrayList<>();
                        for (Object relObj : relArray) {
                            JSONObject r = (JSONObject) relObj;
                            ParsedUMLModel.UMLRelationship relationship = new ParsedUMLModel.UMLRelationship(
                                    (String) r.get("type"),
                                    (String) r.get("target")
                            );
                            relList.add(relationship);
                        }
                        umlClass.setRelationships(relList);
                    }

                    umlClasses.add(umlClass);
                }
                model.setClasses(umlClasses);
            }
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse UML JSON: " + e.getMessage());
        }
        return model;
    }
}
