/*package com.codegen.controller;

import com.codegen.model.entity.ParsedUMLModel;
import com.codegen.model.request.GenerationRequest;
import com.codegen.model.response.GeneratedCodeResponse;
import com.codegen.service.CodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/generator")
public class CodeGeneratorController {

    private final CodeGeneratorService generatorService;

    @Autowired
    public CodeGeneratorController(CodeGeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GeneratedCodeResponse> generateCode(@RequestBody GenerationRequest request) {
        try {
            GeneratedCodeResponse response = new GeneratedCodeResponse();

            // Generate the code
            Map<String, String> files = generatorService.generateFromUML(
                    request.getPlatform(),
                    request.getPattern(),
                    request.getPackageName(),
                    request.getProjectName(),
                    new ParsedUMLModel() // You'll need to parse the UML here
            );

            // Set response properties
            response.setStatus("SUCCESS");
            response.setProjectFiles(files);
            response.setPlatform(request.getPlatform());
            response.setPattern(request.getPattern());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            GeneratedCodeResponse errorResponse = new GeneratedCodeResponse();
            errorResponse.setStatus("ERROR");
            errorResponse.setError(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/supported-platforms")
    public ResponseEntity<String[]> getSupportedPlatforms() {
        return ResponseEntity.ok(new String[]{"android", "ios"});
    }

    @GetMapping("/supported-patterns")
    public ResponseEntity<String[]> getSupportedPatterns() {
        return ResponseEntity.ok(new String[]{"mvvm", "mvc", "mvp"});
    }
}*/
/*package com.codegen.controller;

import com.codegen.model.request.GenerationRequest;
import com.codegen.model.response.GeneratedCodeResponse;
import com.codegen.model.entity.ParsedUMLModel;
import com.codegen.service.CodeGeneratorService;
import com.codegen.service.ProjectStructureGenerator;
import com.codegen.service.UMLTextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/generator")
public class CodeGeneratorController {

    private final CodeGeneratorService generatorService;
    private final ProjectStructureGenerator structureGenerator;
    private final UMLTextParser umlParser;

    @Autowired
    public CodeGeneratorController(
            CodeGeneratorService generatorService,
            ProjectStructureGenerator structureGenerator,
            UMLTextParser umlParser) {
        this.generatorService = generatorService;
        this.structureGenerator = structureGenerator;
        this.umlParser = umlParser;
    }

    @PostMapping("/generate")
    public ResponseEntity<GeneratedCodeResponse> generateCode(@RequestBody GenerationRequest request) {
        try {
            // Parse UML content
            ParsedUMLModel parsedUML = umlParser.parseUMLText(request.getUmlContent());

            // Get project structure
            var projectStructure = structureGenerator.generateProjectStructure(
                    request.getPlatform(),
                    request.getPattern()
            );

            // Generate code files
            Map<String, String> files = generatorService.generateFromUML(
                    request.getPlatform(),
                    request.getPattern(),
                    request.getPackageName(),
                    request.getProjectName(),
                    parsedUML
            );

            // Get dependencies
            Map<String, String> dependencies = structureGenerator.getDependencies(
                    request.getPlatform(),
                    request.getPattern()
            );

            // Create response
            GeneratedCodeResponse response = new GeneratedCodeResponse();
            response.setStatus("SUCCESS");
            response.setProjectFiles(files);
            response.setProjectStructure(projectStructure);
            response.setDependencies(dependencies);
            response.setPlatform(request.getPlatform());
            response.setPattern(request.getPattern());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            GeneratedCodeResponse errorResponse = new GeneratedCodeResponse();
            errorResponse.setStatus("ERROR");
            errorResponse.setError(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/supported-platforms")
    public ResponseEntity<String[]> getSupportedPlatforms() {
        return ResponseEntity.ok(new String[]{"android", "ios"});
    }

    @GetMapping("/supported-patterns")
    public ResponseEntity<String[]> getSupportedPatterns() {
        return ResponseEntity.ok(new String[]{"mvvm", "mvc", "mvp"});
    }
}*/
/*
package com.codegen.controller;

import com.codegen.service.GeminiCodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/generator")
public class CodeGeneratorController {

    private final GeminiCodeGeneratorService generatorService;

    @Autowired
    public CodeGeneratorController(GeminiCodeGeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateCode(@RequestBody GenerateCodeRequest request) {
        try {
            Map<String, String> files = generatorService.generateProject(
                    request.getUmlContent(),
                    request.getPlatform(),
                    request.getPattern(),
                    request.getPackageName(),
                    request.getProjectName()
            );
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    public static class GenerateCodeRequest {
        private String umlContent;
        private String platform;
        private String pattern;
        private String packageName;
        private String projectName;

        // Getters and setters
        public String getUmlContent() { return umlContent; }
        public void setUmlContent(String umlContent) { this.umlContent = umlContent; }

        public String getPlatform() { return platform; }
        public void setPlatform(String platform) { this.platform = platform; }

        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }

        public String getPackageName() { return packageName; }
        public void setPackageName(String packageName) { this.packageName = packageName; }

        public String getProjectName() { return projectName; }
        public void setProjectName(String projectName) { this.projectName = projectName; }
    }
}*/

/* before was working
package com.codegen.controller;

import com.codegen.model.request.GenerationRequest;
import com.codegen.model.response.GeneratedCodeResponse;
import com.codegen.model.entity.ParsedUMLModel;
import com.codegen.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/generator")
public class CodeGeneratorController {

    private final CodeGeneratorService generatorService;
    private final ProjectStructureGenerator structureGenerator;
    private final UMLTextParser umlParser;
    private final GeminiCodeGeneratorService geminiService;

    @Autowired
    public CodeGeneratorController(
            CodeGeneratorService generatorService,
            ProjectStructureGenerator structureGenerator,
            UMLTextParser umlParser,
            GeminiCodeGeneratorService geminiService) {
        this.generatorService = generatorService;
        this.structureGenerator = structureGenerator;
        this.umlParser = umlParser;
        this.geminiService = geminiService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GeneratedCodeResponse> generateCode(@RequestBody GenerationRequest request) {
        try {
            // Parse UML content
            ParsedUMLModel parsedUML = umlParser.parseUMLText(request.getUmlContent());

            // Get project structure
            var projectStructure = structureGenerator.generateProjectStructure(
                    request.getPlatform(),
                    request.getPattern()
            );

            // Generate base code files
            Map<String, String> files = generatorService.generateFromUML(
                    request.getPlatform(),
                    request.getPattern(),
                    request.getPackageName(),
                    request.getProjectName(),
                    parsedUML
            );

            // Generate enhanced code using Gemini for UML-related files
            Map<String, String> enhancedFiles = geminiService.generateProject(
                    request.getUmlContent(),
                    request.getPlatform(),
                    request.getPattern(),
                    request.getPackageName(),
                    request.getProjectName()
            );

            // Merge the enhanced files with the base files
            for (Map.Entry<String, String> entry : enhancedFiles.entrySet()) {
                String path = entry.getKey();
                // Only replace or add UML-related files (models, viewmodels, etc.)
                if (isUMLRelatedFile(path, request.getPlatform())) {
                    files.put(path, entry.getValue());
                }
            }

            // Get dependencies
            Map<String, String> dependencies = structureGenerator.getDependencies(
                    request.getPlatform(),
                    request.getPattern()
            );

            // Create response
            GeneratedCodeResponse response = new GeneratedCodeResponse();
            response.setStatus("SUCCESS");
            response.setProjectFiles(files);
            response.setProjectStructure(projectStructure);
            response.setDependencies(dependencies);
            response.setPlatform(request.getPlatform());
            response.setPattern(request.getPattern());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            GeneratedCodeResponse errorResponse = new GeneratedCodeResponse();
            errorResponse.setStatus("ERROR");
            errorResponse.setError(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    private boolean isUMLRelatedFile(String path, String platform) {
        if (platform.equalsIgnoreCase("android")) {
            return path.contains("/model/") ||
                    path.contains("/viewmodel/") ||
                    path.contains("/repository/") ||
                    path.contains("/dao/") ||
                    path.contains("/presenter/") ||
                    path.contains("/contract/");
        } else {
            return path.contains("/Models/") ||
                    path.contains("/ViewModels/") ||
                    path.contains("/Repositories/") ||
                    path.contains("/Presenters/") ||
                    path.contains("/Protocols/");
        }
    }

    @GetMapping("/supported-platforms")
    public ResponseEntity<String[]> getSupportedPlatforms() {
        return ResponseEntity.ok(new String[]{"android", "ios"});
    }

    @GetMapping("/supported-patterns")
    public ResponseEntity<String[]> getSupportedPatterns() {
        return ResponseEntity.ok(new String[]{"mvvm", "mvc", "mvp"});
    }

    @PostMapping("/preview")
    public ResponseEntity<Map<String, String>> previewUMLFiles(@RequestBody GenerationRequest request) {
        try {
            // Only generate UML-related files for preview
            Map<String, String> files = geminiService.generateProject(
                    request.getUmlContent(),
                    request.getPlatform(),
                    request.getPattern(),
                    request.getPackageName(),
                    request.getProjectName()
            );

            // Filter to only show UML-related files
            Map<String, String> umlFiles = new HashMap<>();
            files.forEach((path, content) -> {
                if (isUMLRelatedFile(path, request.getPlatform())) {
                    umlFiles.put(path, content);
                }
            });

            return ResponseEntity.ok(umlFiles);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}*/
package com.codegen.controller;

import com.codegen.model.request.GenerationRequest;
import com.codegen.model.response.GeneratedCodeResponse;
import com.codegen.model.entity.ParsedUMLModel;
import com.codegen.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/generator")
public class CodeGeneratorController {

    private static final Set<String> IMPORTANT_CONFIG_FILES = Set.of(
            "build.gradle",
            "app/build.gradle",
            "settings.gradle",
            "gradle.properties",
            "app/src/main/AndroidManifest.xml",
            "app/src/main/res/values/strings.xml",
            "app/src/main/res/values/colors.xml",
            "app/src/main/res/values/themes.xml",
            "app/src/main/res/values/styles.xml",
            "app/src/main/res/layout/activity_main.xml",
            "ProjectName/Info.plist",
            "ProjectName.xcodeproj/project.pbxproj",
            "Podfile"
    );

    private final CodeGeneratorService generatorService;
    private final ProjectStructureGenerator structureGenerator;
    private final UMLTextParser umlParser;
    private final GeminiCodeGeneratorService geminiService;

    @Autowired
    public CodeGeneratorController(
            CodeGeneratorService generatorService,
            ProjectStructureGenerator structureGenerator,
            UMLTextParser umlParser,
            GeminiCodeGeneratorService geminiService) {
        this.generatorService = generatorService;
        this.structureGenerator = structureGenerator;
        this.umlParser = umlParser;
        this.geminiService = geminiService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GeneratedCodeResponse> generateCode(@RequestBody GenerationRequest request) {
        try {
            // Parse UML content
            ParsedUMLModel parsedUML = umlParser.parseUMLText(request.getUmlContent());

            // Get project structure
            var projectStructure = structureGenerator.generateProjectStructure(
                    request.getPlatform(),
                    request.getPattern()
            );

            // Generate base code files
            Map<String, String> files = generatorService.generateFromUML(
                    request.getPlatform(),
                    request.getPattern(),
                    request.getPackageName(),
                    request.getProjectName(),
                    parsedUML
            );

            // Generate enhanced code using Gemini
            Map<String, String> enhancedFiles = geminiService.generateProject(
                    request.getUmlContent(),
                    request.getPlatform(),
                    request.getPattern(),
                    request.getPackageName(),
                    request.getProjectName()
            );

            // Merge the enhanced files with the base files
            for (Map.Entry<String, String> entry : enhancedFiles.entrySet()) {
                String path = entry.getKey();
                String normalizedPath = normalizePath(path);
                // Add UML-related files and important config files
                if (isUMLRelatedFile(path, request.getPlatform()) ||
                        IMPORTANT_CONFIG_FILES.contains(normalizedPath)) {
                    files.put(path, entry.getValue());
                }
            }

            // Ensure all important config files are included
            addMissingConfigFiles(files, request);

            // Get dependencies
            Map<String, String> dependencies = structureGenerator.getDependencies(
                    request.getPlatform(),
                    request.getPattern()
            );

            // Create response
            GeneratedCodeResponse response = new GeneratedCodeResponse();
            response.setStatus("SUCCESS");
            response.setProjectFiles(files);
            response.setProjectStructure(projectStructure);
            response.setDependencies(dependencies);
            response.setPlatform(request.getPlatform());
            response.setPattern(request.getPattern());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            GeneratedCodeResponse errorResponse = new GeneratedCodeResponse();
            errorResponse.setStatus("ERROR");
            errorResponse.setError(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    private void addMissingConfigFiles(Map<String, String> files, GenerationRequest request) {
        if (request.getPlatform().equalsIgnoreCase("android")) {
            // Add Android specific config files if missing
            if (!files.containsKey("app/build.gradle")) {
                files.put("app/build.gradle", generateAndroidBuildGradle(request));
            }
            if (!files.containsKey("build.gradle")) {
                files.put("build.gradle", generateRootBuildGradle());
            }
            if (!files.containsKey("settings.gradle")) {
                files.put("settings.gradle", generateSettingsGradle(request.getProjectName()));
            }
            if (!files.containsKey("app/src/main/AndroidManifest.xml")) {
                files.put("app/src/main/AndroidManifest.xml", generateAndroidManifest(request.getPackageName()));
            }
        } else if (request.getPlatform().equalsIgnoreCase("ios")) {
            // Add iOS specific config files if missing
            String infoPlistPath = request.getProjectName() + "/Info.plist";
            if (!files.containsKey(infoPlistPath)) {
                files.put(infoPlistPath, generateInfoPlist());
            }
            if (!files.containsKey("Podfile")) {
                files.put("Podfile", generatePodfile(request));
            }
        }
    }

    private String generateAndroidBuildGradle(GenerationRequest request) {
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

                buildFeatures {
                    viewBinding true
                }

                compileOptions {
                    sourceCompatibility JavaVersion.VERSION_17
                    targetCompatibility JavaVersion.VERSION_17
                }
            }

            dependencies {
                implementation 'androidx.appcompat:appcompat:1.6.1'
                implementation 'com.google.android.material:material:1.9.0'
                implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
                implementation 'androidx.lifecycle:lifecycle-viewmodel:2.6.2'
                implementation 'androidx.lifecycle:lifecycle-livedata:2.6.2'
                implementation 'androidx.room:room-runtime:2.6.1'
                annotationProcessor 'androidx.room:room-compiler:2.6.1'
                implementation 'com.squareup.retrofit2:retrofit:2.9.0'
                implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
                
                testImplementation 'junit:junit:4.13.2'
                androidTestImplementation 'androidx.test.ext:junit:1.1.5'
                androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
            }
            """, request.getPackageName(), request.getPackageName());
    }

    private String generateRootBuildGradle() {
        return """
            buildscript {
                repositories {
                    google()
                    mavenCentral()
                }
                dependencies {
                    classpath 'com.android.tools.build:gradle:8.2.0'
                }
            }

            allprojects {
                repositories {
                    google()
                    mavenCentral()
                }
            }

            tasks.register('clean', Delete) {
                delete rootProject.buildDir
            }
            """;
    }

    private String generateSettingsGradle(String projectName) {
        return String.format("""
            pluginManagement {
                repositories {
                    google()
                    mavenCentral()
                    gradlePluginPortal()
                }
            }
            dependencyResolutionManagement {
                repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
                repositories {
                    google()
                    mavenCentral()
                }
            }

            rootProject.name = "%s"
            include ':app'
            """, projectName);
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

    private String generateInfoPlist() {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
            <plist version="1.0">
            <dict>
                <key>CFBundleDevelopmentRegion</key>
                <string>$(DEVELOPMENT_LANGUAGE)</string>
                <key>CFBundleExecutable</key>
                <string>$(EXECUTABLE_NAME)</string>
                <key>CFBundleIdentifier</key>
                <string>$(PRODUCT_BUNDLE_IDENTIFIER)</string>
                <key>CFBundleInfoDictionaryVersion</key>
                <string>6.0</string>
                <key>CFBundleName</key>
                <string>$(PRODUCT_NAME)</string>
                <key>CFBundlePackageType</key>
                <string>$(PRODUCT_BUNDLE_PACKAGE_TYPE)</string>
                <key>CFBundleShortVersionString</key>
                <string>1.0</string>
                <key>CFBundleVersion</key>
                <string>1</string>
                <key>LSRequiresIPhoneOS</key>
                <true/>
                <key>UIApplicationSceneManifest</key>
                <dict>
                    <key>UIApplicationSupportsMultipleScenes</key>
                    <false/>
                </dict>
                <key>UILaunchStoryboardName</key>
                <string>LaunchScreen</string>
                <key>UISupportedInterfaceOrientations</key>
                <array>
                    <string>UIInterfaceOrientationPortrait</string>
                </array>
            </dict>
            </plist>
            """;
    }

    private String generatePodfile(GenerationRequest request) {
        return String.format("""
            platform :ios, '14.0'
            use_frameworks!

            target '%s' do
              pod 'RxSwift', '~> 6.5.0'
              pod 'RxCocoa', '~> 6.5.0'
              pod 'SnapKit', '~> 5.6.0'
              pod 'Alamofire', '~> 5.8.1'
              
              target '%sTests' do
                inherit! :search_paths
                pod 'Quick'
                pod 'Nimble'
              end
            end
            """, request.getProjectName(), request.getProjectName());
    }

    private boolean isUMLRelatedFile(String path, String platform) {
        if (platform.equalsIgnoreCase("android")) {
            return path.contains("/model/") ||
                    path.contains("/viewmodel/") ||
                    path.contains("/repository/") ||
                    path.contains("/dao/") ||
                    path.contains("/presenter/") ||
                    path.contains("/contract/");
        } else {
            return path.contains("/Models/") ||
                    path.contains("/ViewModels/") ||
                    path.contains("/Repositories/") ||
                    path.contains("/Presenters/") ||
                    path.contains("/Protocols/");
        }
    }

    private String normalizePath(String path) {
        return path.replaceAll("^/+", "").replaceAll("/+$", "");
    }

    @GetMapping("/supported-platforms")
    public ResponseEntity<String[]> getSupportedPlatforms() {
        return ResponseEntity.ok(new String[]{"android", "ios"});
    }

    @GetMapping("/supported-patterns")
    public ResponseEntity<String[]> getSupportedPatterns() {
        return ResponseEntity.ok(new String[]{"mvvm", "mvc", "mvp"});
    }
}