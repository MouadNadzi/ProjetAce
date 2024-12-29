// backend/src/main/java/com/codegen/service/DiagramProcessorService.java
package com.codegen.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import com.codegen.helper.CodeExtractor;
import java.util.HashMap;
import java.util.List;

@Service
public class DiagramProcessorService {

    @Value("${gemini.api.key}")
    private String API_KEY;

    private static final String GEMINI_MODEL = "gemini-1.5-flash";
    @SuppressWarnings("unchecked")

    public HashMap<String, List<HashMap<String, String>>> processDiagram(
            String base64Image,
            String platform,
            String architecture) {

        String prompt = generatePromptForArchitecture(platform, architecture);
        String payload = createImagePromptPayload(base64Image, prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();

        String url = String.format(
                "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                GEMINI_MODEL, API_KEY
        );

        try {
            JSONObject response = restTemplate.postForObject(url, request, JSONObject.class);
            String extractedText = CodeExtractor.extractText(response);
            return CodeExtractor.parseCodeFiles(extractedText, platform, architecture);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process diagram: " + e.getMessage(), e);
        }
    }

    private String generatePromptForArchitecture(String platform, String architecture) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze this class diagram and provide a complete implementation for a ");
        prompt.append(platform).append(" application using the ").append(architecture).append(" pattern.\n\n");

        if (platform.equalsIgnoreCase("android")) {
            switch (architecture.toLowerCase()) {
                case "mvc":
                    prompt.append("Include:\n")
                            .append("1. Model classes with proper data structures\n")
                            .append("2. Controller classes handling business logic\n")
                            .append("3. Activity/Fragment classes as Views\n")
                            .append("4. SQLite database implementation\n")
                            .append("5. Adapter classes for RecyclerViews if needed\n");
                    break;

                case "mvvm":
                    prompt.append("Include:\n")
                            .append("1. Model classes and Room entities\n")
                            .append("2. ViewModel classes with LiveData/StateFlow\n")
                            .append("3. Repository classes\n")
                            .append("4. Activity/Fragment classes with data binding\n")
                            .append("5. Room DAO interfaces\n");
                    break;

                case "mvp":
                    prompt.append("Include:\n")
                            .append("1. Model classes with data structures\n")
                            .append("2. Presenter classes with interfaces\n")
                            .append("3. View interfaces and implementations\n")
                            .append("4. Contract interfaces for View-Presenter communication\n")
                            .append("5. Repository pattern implementation\n");
                    break;
            }
        } else if (platform.equalsIgnoreCase("ios")) {
            switch (architecture.toLowerCase()) {
                case "mvc":
                    prompt.append("Include:\n")
                            .append("1. Model classes with Codable\n")
                            .append("2. Controller classes (UIViewControllers)\n")
                            .append("3. View classes with UIKit\n")
                            .append("4. CoreData implementation\n")
                            .append("5. TableView/CollectionView implementations\n");
                    break;

                case "mvvm":
                    prompt.append("Include:\n")
                            .append("1. Model classes with Codable\n")
                            .append("2. ViewModel classes with Combine\n")
                            .append("3. View classes with SwiftUI\n")
                            .append("4. Repository pattern implementation\n")
                            .append("5. CoreData or Realm integration\n");
                    break;

                case "mvp":
                    prompt.append("Include:\n")
                            .append("1. Model classes\n")
                            .append("2. Presenter classes with protocols\n")
                            .append("3. View protocols and implementations\n")
                            .append("4. Contract protocols for View-Presenter communication\n")
                            .append("5. Data management layer\n");
                    break;
            }
        }

        prompt.append("\nFor each file, start with '#' followed by the filename and extension (e.g., #PersonModel.java or #PersonModel.swift), ")
                .append("then provide the complete code implementation. Include all necessary imports, annotations, and dependencies.\n")
                .append("Ensure the code follows best practices for the chosen platform and architecture pattern.");

        return prompt.toString();
    }

    private String createImagePromptPayload(String base64Image, String promptText) {
        JSONObject root = new JSONObject();
        JSONArray contents = new JSONArray();
        JSONObject content = new JSONObject();
        JSONArray parts = new JSONArray();

        // Add text prompt
        JSONObject textPart = new JSONObject();
        textPart.put("text", promptText);
        parts.add(textPart);

        // Add image data
        JSONObject imagePart = new JSONObject();
        JSONObject inlineData = new JSONObject();
        inlineData.put("mime_type", "image/jpeg");
        inlineData.put("data", base64Image);
        imagePart.put("inline_data", inlineData);
        parts.add(imagePart);

        content.put("parts", parts);
        contents.add(content);
        root.put("contents", contents);

        // Add generation config for code generation
        JSONObject generationConfig = new JSONObject();
        generationConfig.put("temperature", 0.3); // Lower temperature for more consistent code
        generationConfig.put("topP", 0.8);
        generationConfig.put("topK", 40);
        generationConfig.put("maxOutputTokens", 8192); // For longer code generation
        root.put("generationConfig", generationConfig);

        return root.toJSONString();
    }
}