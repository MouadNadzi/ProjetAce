/*package com.codegen.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.*;

@Service
public class GeminiCodeGeneratorService {

    @Value("${gemini.api.key:YOUR_API_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final String baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    public GeminiCodeGeneratorService() {
        this.restTemplate = new RestTemplate();
    }

    public String generateCode(String umlContent, String platform, String pattern, String component) {
        String prompt = buildPrompt(umlContent, platform, pattern, component);
        return callGeminiApi(prompt);
    }

    public Map<String, String> generateModelClass(String umlContent, String className, String platform, String pattern) {
        String prompt = String.format("""
            Generate a complete model class for:
            Platform: %s
            Pattern: %s
            Class name: %s
            
            UML Definition:
            %s
            
            Requirements:
            - Include all necessary annotations
            - Add proper validation
            - Include relationship mappings
            - Add builder pattern
            - Include all necessary imports
            - Add comprehensive documentation
            - Include equals/hashCode/toString
            
            Return only the code without any explanation.
            """,
                platform, pattern, className, umlContent
        );

        String generatedCode = callGeminiApi(prompt);
        return Map.of(className + getFileExtension(platform), generatedCode);
    }

    public Map<String, String> generateViewModel(String umlContent, String className, String platform) {
        String prompt = String.format("""
            Generate a complete ViewModel/Presenter for:
            Platform: %s
            Class name: %s
            
            UML Definition:
            %s
            
            Requirements:
            - Use proper lifecycle management
            - Include state handling
            - Add error handling
            - Include data validation
            - Add proper documentation
            - Include unit test structure
            - Use coroutines/RxJava for Android or Combine for iOS
            
            Return only the code without any explanation.
            """,
                platform, className, umlContent
        );

        String generatedCode = callGeminiApi(prompt);
        return Map.of(className + "ViewModel" + getFileExtension(platform), generatedCode);
    }

    private String callGeminiApi(String prompt) {
        try {
            // Build request body
            JSONObject requestBody = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();
            JSONArray parts = new JSONArray();

            // Add text part
            JSONObject textPart = new JSONObject();
            textPart.put("text", prompt);
            parts.add(textPart);

            content.put("parts", parts);
            contents.add(content);
            requestBody.put("contents", contents);

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Make request
            String url = baseUrl + "?key=" + apiKey;
            HttpEntity<String> request = new HttpEntity<>(requestBody.toJSONString(), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return extractTextFromResponse(response.getBody());
            } else {
                throw new RuntimeException("Failed to generate code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate code: " + e.getMessage());
        }
    }

    private String extractTextFromResponse(String responseBody) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(responseBody);
            JSONArray candidates = (JSONArray) root.get("candidates");

            if (candidates != null && !candidates.isEmpty()) {
                JSONObject firstCandidate = (JSONObject) candidates.get(0);
                JSONObject content = (JSONObject) firstCandidate.get("content");
                JSONArray parts = (JSONArray) content.get("parts");

                if (parts != null && !parts.isEmpty()) {
                    JSONObject firstPart = (JSONObject) parts.get(0);
                    return (String) firstPart.get("text");
                }
            }

            throw new RuntimeException("No content found in response");
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse response: " + e.getMessage());
        }
    }

    private String buildPrompt(String umlContent, String platform, String pattern, String component) {
        return String.format("""
            You are an expert mobile developer. Generate production-ready %s code for a %s %s application.
            
            UML Model:
            %s
            
            Requirements:
            - Follow clean architecture principles
            - Include proper error handling and validation
            - Add comprehensive documentation
            - Include unit test structure
            - Handle all relationships correctly
            - Follow platform best practices
            - Use modern libraries and patterns
            
            The code should be complete and ready to use. Include all necessary imports and annotations.
            Return only the code without any explanation or markdown formatting.
            """,
                component, platform, pattern, umlContent
        );
    }

    private String getFileExtension(String platform) {
        return platform.equalsIgnoreCase("android") ? ".java" : ".swift";
    }
}*/
package com.codegen.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.*;

@Service
public class GeminiCodeGeneratorService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ProjectStructureGenerator structureGenerator;
    private final String baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    public GeminiCodeGeneratorService(ProjectStructureGenerator structureGenerator) {
        this.restTemplate = new RestTemplate();
        this.structureGenerator = structureGenerator;
    }

    public Map<String, String> generateProject(String umlContent, String platform, String pattern, String packageName, String projectName) {
        Map<String, String> projectFiles = new HashMap<>();

        // 1. Generate project structure
        List<String> structure = structureGenerator.generateProjectStructure(platform, pattern);
        for (String dir : structure) {
            projectFiles.put(dir + "/.gitkeep", ""); // Create empty files to maintain directory structure
        }

        // 2. Parse UML content to get class names
        List<String> classNames = extractClassNames(umlContent);

        // 3. Generate code for each class
        for (String className : classNames) {
            Map<String, String> classFiles = generateClassFiles(
                    umlContent,
                    className,
                    platform,
                    pattern,
                    packageName,
                    projectName
            );
            projectFiles.putAll(classFiles);
        }

        // 4. Generate additional files
        generateAdditionalFiles(projectFiles, platform, pattern, packageName, projectName);

        return projectFiles;
    }

    private Map<String, String> generateClassFiles(
            String umlContent,
            String className,
            String platform,
            String pattern,
            String packageName,
            String projectName
    ) {
        Map<String, String> files = new HashMap<>();

        if (platform.equalsIgnoreCase("android")) {
            // Generate Android files
            String modelPath = String.format("app/src/main/java/%s/model/%s.java",
                    packageName.replace('.', '/'), className);
            files.put(modelPath, generateAndroidModel(umlContent, className));

            if (pattern.equalsIgnoreCase("mvvm")) {
                String viewModelPath = String.format("app/src/main/java/%s/viewmodel/%sViewModel.java",
                        packageName.replace('.', '/'), className);
                files.put(viewModelPath, generateAndroidViewModel(umlContent, className, packageName));

                String repositoryPath = String.format("app/src/main/java/%s/repository/%sRepository.java",
                        packageName.replace('.', '/'), className);
                files.put(repositoryPath, generateAndroidRepository(umlContent, className, packageName));
            }
        } else if (platform.equalsIgnoreCase("ios")) {
            // Generate iOS files
            String modelPath = String.format("%s/Models/%s.swift", projectName, className);
            files.put(modelPath, generateIOSModel(umlContent, className));

            if (pattern.equalsIgnoreCase("mvvm")) {
                String viewModelPath = String.format("%s/ViewModels/%sViewModel.swift", projectName, className);
                files.put(viewModelPath, generateIOSViewModel(umlContent, className));
            }
        }

        return files;
    }

    private String generateAndroidModel(String umlContent, String className) {
        String prompt = String.format("""
            Generate a complete Android Room entity class for the following UML definition:
            %s
            
            Focus on the %s class. Include:
            - All Room annotations
            - Builder pattern
            - Relationship handling
            - All required imports
            - equals/hashCode/toString
            - Proper validation
            
            Return only the code without any explanation or markdown formatting.
            """, umlContent, className);

        return callGeminiApi(prompt);
    }

    private String generateAndroidViewModel(String umlContent, String className, String packageName) {
        String prompt = String.format("""
            Generate an Android ViewModel for:
            Package: %s
            Class: %s
            
            UML:
            %s
            
            Include:
            - LiveData/StateFlow usage
            - Repository integration
            - Error handling
            - All required imports
            - Proper documentation
            - Coroutines implementation
            
            Return only the code without any explanation or markdown formatting.
            """, packageName, className, umlContent);

        return callGeminiApi(prompt);
    }

    private String generateAndroidRepository(String umlContent, String className, String packageName) {
        String prompt = String.format("""
            Generate an Android Repository for:
            Package: %s
            Class: %s
            
            UML:
            %s
            
            Include:
            - Room DAO integration
            - Coroutines implementation
            - Error handling
            - Caching if appropriate
            - All required imports
            - Proper documentation
            
            Return only the code without any explanation or markdown formatting.
            """, packageName, className, umlContent);

        return callGeminiApi(prompt);
    }

    private void generateAdditionalFiles(
            Map<String, String> files,
            String platform,
            String pattern,
            String packageName,
            String projectName
    ) {
        if (platform.equalsIgnoreCase("android")) {
            // Add build.gradle
            files.put("app/build.gradle", generateBuildGradle(packageName));

            // Add AndroidManifest.xml
            String manifestPath = "app/src/main/AndroidManifest.xml";
            files.put(manifestPath, generateAndroidManifest(packageName));

            // Add base Activity
            String activityPath = String.format("app/src/main/java/%s/MainActivity.java",
                    packageName.replace('.', '/'));
            files.put(activityPath, generateMainActivity(packageName, pattern));

            // Add layout files
            files.put("app/src/main/res/layout/activity_main.xml", generateMainLayout());
        } else if (platform.equalsIgnoreCase("ios")) {
            // Add iOS specific files
            files.put(projectName + "/AppDelegate.swift", generateIOSAppDelegate(projectName));
            files.put(projectName + "/SceneDelegate.swift", generateIOSSceneDelegate(projectName));
        }
    }

    private String generateIOSModel(String umlContent, String className) {
        String prompt = String.format("""
            Generate a complete Swift model class for:
            Class name: %s
            
            UML Definition:
            %s
            
            Include:
            - Codable conformance
            - Property wrappers if needed
            - Relationship handling
            - Proper validation
            - Equatable conformance
            - Custom initializers
            - Proper documentation
            
            Return only the code without any explanation or markdown formatting.
            """, className, umlContent);

        return callGeminiApi(prompt);
    }

    private String generateIOSViewModel(String umlContent, String className) {
        String prompt = String.format("""
            Generate a complete Swift ViewModel for:
            Class name: %s
            
            UML Definition:
            %s
            
            Include:
            - ObservableObject conformance
            - Published properties
            - Error handling
            - Combine implementation
            - Network calls
            - Proper documentation
            - Unit test preparation
            
            Return only the code without any explanation or markdown formatting.
            """, className, umlContent);

        return callGeminiApi(prompt);
    }

    private String generateIOSAppDelegate(String projectName) {
        String prompt = String.format("""
            Generate a complete Swift AppDelegate for iOS project:
            Project name: %s
            
            Include:
            - Basic app lifecycle methods
            - Basic setup code
            - Proper documentation
            - SwiftUI integration if needed
            
            Return only the code without any explanation or markdown formatting.
            """, projectName);

        return callGeminiApi(prompt);
    }

    private String generateIOSSceneDelegate(String projectName) {
        String prompt = String.format("""
            Generate a complete Swift SceneDelegate for iOS project:
            Project name: %s
            
            Include:
            - Scene lifecycle methods
            - Window setup
            - Initial view controller setup
            - SwiftUI integration if needed
            - Proper documentation
            
            Return only the code without any explanation or markdown formatting.
            """, projectName);

        return callGeminiApi(prompt);
    }

    private String callGeminiApi(String prompt) {
        try {
            JSONObject requestBody = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();
            JSONArray parts = new JSONArray();

            JSONObject textPart = new JSONObject();
            textPart.put("text", prompt);
            parts.add(textPart);

            content.put("parts", parts);
            contents.add(content);
            requestBody.put("contents", contents);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String url = baseUrl + "?key=" + apiKey;
            HttpEntity<String> request = new HttpEntity<>(requestBody.toJSONString(), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return extractTextFromResponse(response.getBody());
            } else {
                throw new RuntimeException("Failed to generate code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate code: " + e.getMessage());
        }
    }

    private String extractTextFromResponse(String responseBody) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(responseBody);
            JSONArray candidates = (JSONArray) root.get("candidates");

            if (candidates != null && !candidates.isEmpty()) {
                JSONObject firstCandidate = (JSONObject) candidates.get(0);
                JSONObject content = (JSONObject) firstCandidate.get("content");
                JSONArray parts = (JSONArray) content.get("parts");

                if (parts != null && !parts.isEmpty()) {
                    JSONObject firstPart = (JSONObject) parts.get(0);
                    String text = (String) firstPart.get("text");
                    // Remove markdown code blocks if present
                    return text.replaceAll("```\\w*\\n", "").replaceAll("```", "");
                }
            }

            throw new RuntimeException("No content found in response");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response: " + e.getMessage());
        }
    }

    private List<String> extractClassNames(String umlContent) {
        List<String> classNames = new ArrayList<>();
        String[] lines = umlContent.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("model")) {
                String className = line.substring(6, line.indexOf('{')).trim();
                classNames.add(className);
            }
        }
        return classNames;
    }

    // Add helper methods for generating additional files (build.gradle, manifest, etc.)
    private String generateBuildGradle(String packageName) {
        return String.format("""
            plugins {
                id 'com.android.application'
            }
            
            android {
                namespace '%s'
                compileSdk 34
                
                defaultConfig {
                    applicationId "%s"
                    minSdk 24
                    targetSdk 34
                    versionCode 1
                    versionName "1.0"
                }
                
                buildTypes {
                    release {
                        minifyEnabled false
                        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
                    }
                }
                
                compileOptions {
                    sourceCompatibility JavaVersion.VERSION_1_8
                    targetCompatibility JavaVersion.VERSION_1_8
                }
            }
            
            dependencies {
                implementation 'androidx.appcompat:appcompat:1.6.1'
                implementation 'androidx.room:room-runtime:2.6.1'
                annotationProcessor 'androidx.room:room-compiler:2.6.1'
                implementation 'androidx.lifecycle:lifecycle-viewmodel:2.6.2'
                implementation 'androidx.lifecycle:lifecycle-livedata:2.6.2'
            }
            """, packageName, packageName);
    }

    private String generateAndroidManifest(String packageName) {
        return String.format("""
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                package="%s">
                
                <application
                    android:allowBackup="true"
                    android:icon="@mipmap/ic_launcher"
                    android:label="@string/app_name"
                    android:roundIcon="@mipmap/ic_launcher_round"
                    android:supportsRtl="true"
                    android:theme="@style/Theme.AppCompat.Light.DarkActionBar">
                    
                    <activity
                        android:name=".MainActivity"
                        android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.MAIN" />
                            <category android:name="android.intent.category.LAUNCHER" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>
            """, packageName);
    }

    private String generateMainActivity(String packageName, String pattern) {
        return String.format("""
            package %s;
            
            import android.os.Bundle;
            import androidx.appcompat.app.AppCompatActivity;
            
            public class MainActivity extends AppCompatActivity {
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_main);
                }
            }
            """, packageName);
    }

    private String generateMainLayout() {
        return """
            <?xml version="1.0" encoding="utf-8"?>
            <androidx.constraintlayout.widget.ConstraintLayout
                xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:app="http://schemas.android.com/apk/res-auto"
                android:layout_width="match_parent"
                android:layout_height="match_parent">
                
                <!-- Add your layout elements here -->
                
            </androidx.constraintlayout.widget.ConstraintLayout>
            """;
    }
}