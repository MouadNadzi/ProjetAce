package com.codegen.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Service
public class ProjectStructureService {

    public Map<String, String> generateBaseStructure(
            String platform,
            String pattern,
            String packageName,
            String projectName
    ) {
        Map<String, String> files = new HashMap<>();

        if (platform.equalsIgnoreCase("android")) {
            // Add Android base files
            files.putAll(generateAndroidBaseFiles(packageName, projectName, pattern));
        } else if (platform.equalsIgnoreCase("ios")) {
            // Add iOS base files
            files.putAll(generateIOSBaseFiles(projectName, pattern));
        }

        return files;
    }

    private Map<String, String> generateAndroidBaseFiles(
            String packageName,
            String projectName,
            String pattern
    ) {
        Map<String, String> files = new HashMap<>();

        // Add build.gradle
        files.put("app/build.gradle", generateAndroidBuildGradle(packageName, projectName));

        // Add AndroidManifest.xml
        files.put("app/src/main/AndroidManifest.xml", generateAndroidManifest(packageName));

        // Add MainActivity
        String mainActivityPath = String.format(
                "app/src/main/java/%s/MainActivity.java",
                packageName.replace('.', '/')
        );
        files.put(mainActivityPath, generateAndroidMainActivity(packageName, pattern));

        return files;
    }

    private String generateAndroidMainActivity(String packageName, String pattern) {
        return String.format("""
            package %s;
            
            import android.os.Bundle;
            import androidx.appcompat.app.AppCompatActivity;
            
            public class MainActivity extends AppCompatActivity {
                
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_main);
                    
                    %s
                }
                
                %s
            }
            """,
                packageName,
                generateMainActivitySetup(pattern),
                generateMainActivityMethods(pattern)
        );
    }

    private String generateMainActivitySetup(String pattern) {
        return switch (pattern.toLowerCase()) {
            case "mvvm" -> """
                // Initialize ViewModel
                MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
                
                // Observe ViewModel data
                viewModel.getData().observe(this, data -> {
                    // Handle data updates
                });
                """;
            case "mvp" -> """
                // Initialize Presenter
                MainPresenter presenter = new MainPresenter(this);
                
                // Load initial data
                presenter.loadData();
                """;
            case "mvc" -> """
                // Initialize Controller
                MainController controller = new MainController();
                
                // Setup views
                setupViews();
                """;
            default -> throw new IllegalArgumentException("Unsupported pattern: " + pattern);
        };
    }

    private String generateMainActivityMethods(String pattern) {
        return switch (pattern.toLowerCase()) {
            case "mvvm" -> """
                private void setupViews() {
                    // Setup RecyclerView, adapters, etc.
                }
                
                private void handleDataUpdate(Data data) {
                    // Update UI with new data
                }
                """;
            case "mvp" -> """
                // MVP View interface implementation
                @Override
                public void showLoading() {
                    // Show loading indicator
                }
                
                @Override
                public void hideLoading() {
                    // Hide loading indicator
                }
                
                @Override
                public void showError(String message) {
                    // Show error message
                }
                
                @Override
                public void showData(Data data) {
                    // Display data
                }
                """;
            case "mvc" -> """
                private void setupViews() {
                    // Initialize views
                }
                
                private void handleUserAction() {
                    // Handle user interactions
                }
                """;
            default -> throw new IllegalArgumentException("Unsupported pattern: " + pattern);
        };
    }

    private String generateAndroidBuildGradle(String packageName, String projectName) {
        return """
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
                        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
                    }
                }
                compileOptions {
                    sourceCompatibility JavaVersion.VERSION_11
                    targetCompatibility JavaVersion.VERSION_11
                }
            }
            
            dependencies {
                implementation 'androidx.appcompat:appcompat:1.6.1'
                implementation 'com.google.android.material:material:1.9.0'
                implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
                testImplementation 'junit:junit:4.13.2'
                androidTestImplementation 'androidx.test.ext:junit:1.1.5'
                androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
            }
            """.formatted(packageName, packageName);
    }

    private String generateAndroidManifest(String packageName) {
        return """
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
            """.formatted(packageName);
    }

    private Map<String, String> generateIOSBaseFiles(String projectName, String pattern) {
        Map<String, String> files = new HashMap<>();

        // Add AppDelegate
        files.put(projectName + "/AppDelegate.swift", generateIOSAppDelegate(projectName));

        // Add SceneDelegate
        files.put(projectName + "/SceneDelegate.swift", generateIOSSceneDelegate(projectName));

        // Add Info.plist
        files.put(projectName + "/Info.plist", generateIOSInfoPlist(projectName));

        return files;
    }

    private String generateIOSAppDelegate(String projectName) {
        return """
            import UIKit
            
            @main
            class AppDelegate: UIResponder, UIApplicationDelegate {
                func application(
                    _ application: UIApplication,
                    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
                ) -> Bool {
                    return true
                }
                
                func application(
                    _ application: UIApplication,
                    configurationForConnecting connectingSceneSession: UISceneSession,
                    options: UIScene.ConnectionOptions
                ) -> UISceneConfiguration {
                    return UISceneConfiguration(
                        name: "Default Configuration",
                        sessionRole: connectingSceneSession.role
                    )
                }
            }
            """;
    }

    private String generateIOSSceneDelegate(String projectName) {
        return """
            import UIKit
            
            class SceneDelegate: UIResponder, UIWindowSceneDelegate {
                var window: UIWindow?
                
                func scene(
                    _ scene: UIScene,
                    willConnectTo session: UISceneSession,
                    options connectionOptions: UIScene.ConnectionOptions
                ) {
                    guard let windowScene = (scene as? UIWindowScene) else { return }
                    
                    window = UIWindow(windowScene: windowScene)
                    window?.rootViewController = UINavigationController(
                        rootViewController: ViewController()
                    )
                    window?.makeKeyAndVisible()
                }
            }
            """;
    }

    private String generateIOSInfoPlist(String projectName) {
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
                    <key>UISceneConfigurations</key>
                    <dict>
                        <key>UIWindowSceneSessionRoleApplication</key>
                        <array>
                            <dict>
                                <key>UISceneConfigurationName</key>
                                <string>Default Configuration</string>
                                <key>UISceneDelegateClassName</key>
                                <string>$(PRODUCT_MODULE_NAME).SceneDelegate</string>
                            </dict>
                        </array>
                    </dict>
                </dict>
                <key>UILaunchStoryboardName</key>
                <string>LaunchScreen</string>
                <key>UIRequiredDeviceCapabilities</key>
                <array>
                    <string>armv7</string>
                </array>
                <key>UISupportedInterfaceOrientations</key>
                <array>
                    <string>UIInterfaceOrientationPortrait</string>
                </array>
            </dict>
            </plist>
            """;
    }
}