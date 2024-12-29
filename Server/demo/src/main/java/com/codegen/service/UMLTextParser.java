package com.codegen.service;

import com.codegen.model.entity.ParsedUMLModel;
import org.springframework.stereotype.Service;
import java.util.regex.*;
import java.util.*;

@Service
public class UMLTextParser {

    public ParsedUMLModel parseUMLText(String umlText) {
        ParsedUMLModel model = new ParsedUMLModel();
        Scanner scanner = new Scanner(umlText);
        ParsedUMLModel.UMLClass currentClass = null;

        Pattern modelPattern = Pattern.compile("model\\s+(\\w+)\\s*\\{");
        Pattern fieldPattern = Pattern.compile("\\s*(\\w+):\\s*(\\w+)(?:\\s+(\\w+))*");
        Pattern linkPattern = Pattern.compile("link\\s+(\\w+)\\s*->\\s*(\\w+)\\s*\\((.+)\\)");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            // Skip empty lines and comments
            if (line.isEmpty() || line.startsWith("//")) {
                continue;
            }

            // Parse model declaration
            Matcher modelMatcher = modelPattern.matcher(line);
            if (modelMatcher.find()) {
                String className = modelMatcher.group(1);
                currentClass = new ParsedUMLModel.UMLClass(className);
                model.addClass(currentClass);
                continue;
            }

            // Parse fields
            if (currentClass != null && !line.equals("}")) {
                Matcher fieldMatcher = fieldPattern.matcher(line);
                if (fieldMatcher.find()) {
                    String fieldName = fieldMatcher.group(1);
                    String fieldType = fieldMatcher.group(2);

                    // Parse modifiers
                    boolean required = line.contains("required");
                    boolean unique = line.contains("unique");

                    ParsedUMLModel.UMLField field = new ParsedUMLModel.UMLField(
                            fieldName,
                            convertType(fieldType),
                            required,
                            unique
                    );
                    currentClass.addField(field);
                }
            }

            // Parse relationships
            Matcher linkMatcher = linkPattern.matcher(line);
            if (linkMatcher.find()) {
                String sourceClass = linkMatcher.group(1);
                String targetClass = linkMatcher.group(2);
                String relationship = linkMatcher.group(3).trim();

                model.addRelationship(new ParsedUMLModel.UMLRelationship(
                        sourceClass,
                        targetClass,
                        "link",
                        relationship
                ));
            }
        }

        return model;
    }

    private String convertType(String umlType) {
        return switch (umlType.toLowerCase()) {
            case "text" -> "String";
            case "number" -> "Integer";
            case "date" -> "Date";
            case "file" -> "String"; // File paths stored as strings
            case "long" -> umlType;
            default -> umlType;
        };
    }
}