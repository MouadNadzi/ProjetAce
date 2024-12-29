// backend/src/main/java/com/codegen/helper/CodeExtractor.java
package com.codegen.helper;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.*;

public class CodeExtractor {

    public static String extractText(JSONObject response) {
        try {
            JSONArray candidates = (JSONArray) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                JSONObject candidate = (JSONObject) candidates.get(0);
                JSONObject content = (JSONObject) candidate.get("content");
                if (content != null) {
                    JSONArray parts = (JSONArray) content.get("parts");
                    if (parts != null && !parts.isEmpty()) {
                        JSONObject firstPart = (JSONObject) parts.get(0);
                        return (String) firstPart.get("text");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract text from response", e);
        }
        return "";
    }

    public static HashMap<String, List<HashMap<String, String>>> parseCodeFiles(
            String text,
            String platform,
            String architecture) {

        HashMap<String, List<HashMap<String, String>>> result = initializeResultMap(platform, architecture);

        StringBuilder currentFileName = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();
        boolean readingFileName = false;
        boolean readingCode = false;

        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.startsWith("#")) {
                // Save previous file if exists
                if (currentFileName.length() > 0 && currentCode.length() > 0) {
                    saveCode(result, currentFileName.toString(), currentCode.toString(), platform, architecture);
                }
                // Start new file
                currentFileName = new StringBuilder(line.substring(1).trim());
                currentCode = new StringBuilder();
                readingFileName = false;
                readingCode = true;
            } else if (readingCode) {
                currentCode.append(line).append("\n");
            }
        }

        // Save last file
        if (currentFileName.length() > 0 && currentCode.length() > 0) {
            saveCode(result, currentFileName.toString(), currentCode.toString(), platform, architecture);
        }

        return result;
    }

    private static HashMap<String, List<HashMap<String, String>>> initializeResultMap(
            String platform,
            String architecture) {

        HashMap<String, List<HashMap<String, String>>> result = new HashMap<>();

        if (platform.equalsIgnoreCase("android")) {
            switch (architecture.toLowerCase()) {
                case "mvc":
                    result.put("model", new ArrayList<>());
                    result.put("controller", new ArrayList<>());
                    result.put("view", new ArrayList<>());
                    result.put("adapter", new ArrayList<>());
                    result.put("database", new ArrayList<>());
                    break;

                case "mvvm":
                    result.put("model", new ArrayList<>());
                    result.put("viewmodel", new ArrayList<>());
                    result.put("view", new ArrayList<>());
                    result.put("repository", new ArrayList<>());
                    result.put("dao", new ArrayList<>());
                    break;

                case "mvp":
                    result.put("model", new ArrayList<>());
                    result.put("presenter", new ArrayList<>());
                    result.put("view", new ArrayList<>());
                    result.put("contract", new ArrayList<>());
                    result.put("repository", new ArrayList<>());
                    break;
            }
        } else if (platform.equalsIgnoreCase("ios")) {
            switch (architecture.toLowerCase()) {
                case "mvc":
                    result.put("model", new ArrayList<>());
                    result.put("controller", new ArrayList<>());
                    result.put("view", new ArrayList<>());
                    result.put("coredata", new ArrayList<>());
                    break;

                case "mvvm":
                    result.put("model", new ArrayList<>());
                    result.put("viewmodel", new ArrayList<>());
                    result.put("view", new ArrayList<>());
                    result.put("repository", new ArrayList<>());
                    break;

                case "mvp":
                    result.put("model", new ArrayList<>());
                    result.put("presenter", new ArrayList<>());
                    result.put("view", new ArrayList<>());
                    result.put("protocol", new ArrayList<>());
                    break;
            }
        }

        return result;
    }

    private static void saveCode(
            HashMap<String, List<HashMap<String, String>>> result,
            String fileName,
            String code,
            String platform,
            String architecture) {

        HashMap<String, String> fileMap = new HashMap<>();
        fileMap.put(fileName, code.trim());

        String fileType = determineFileType(fileName.toLowerCase(), platform, architecture);
        if (result.containsKey(fileType)) {
            result.get(fileType).add(fileMap);
        }
    }

    private static String determineFileType(
            String fileName,
            String platform,
            String architecture) {

        if (platform.equalsIgnoreCase("android")) {
            if (fileName.contains("activity") || fileName.contains("fragment")) return "view";
            if (fileName.contains("adapter")) return "adapter";
            if (fileName.contains("database")) return "database";
            if (fileName.contains("dao")) return "dao";

            switch (architecture.toLowerCase()) {
                case "mvvm":
                    if (fileName.contains("viewmodel")) return "viewmodel";
                    if (fileName.contains("repository")) return "repository";
                    break;
                case "mvp":
                    if (fileName.contains("presenter")) return "presenter";
                    if (fileName.contains("contract")) return "contract";
                    break;
                case "mvc":
                    if (fileName.contains("controller")) return "controller";
                    break;
            }
        } else if (platform.equalsIgnoreCase("ios")) {
            if (fileName.contains("viewcontroller")) return "controller";
            if (fileName.contains("view")) return "view";
            if (fileName.contains("coredata")) return "coredata";

            switch (architecture.toLowerCase()) {
                case "mvvm":
                    if (fileName.contains("viewmodel")) return "viewmodel";
                    if (fileName.contains("repository")) return "repository";
                    break;
                case "mvp":
                    if (fileName.contains("presenter")) return "presenter";
                    if (fileName.contains("protocol")) return "protocol";
                    break;
            }
        }

        return "model"; // Default category
    }
}