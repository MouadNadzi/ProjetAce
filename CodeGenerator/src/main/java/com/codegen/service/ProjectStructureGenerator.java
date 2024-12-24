/*package com.codegen.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProjectStructureGenerator {

    public List<String> generateProjectStructure(String platform, String pattern) {
        List<String> structure = new ArrayList<>();

        switch (platform.toLowerCase()) {
            case "android":
                switch (pattern.toLowerCase()) {
                    case "mvvm":
                        structure.addAll(generateAndroidMVVMStructure());
                        break;
                    case "mvc":
                        structure.addAll(generateAndroidMVCStructure());
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported pattern for Android: " + pattern);
                }
                break;

            case "ios":
                switch (pattern.toLowerCase()) {
                    case "mvvm":
                        structure.addAll(generateIOSMVVMStructure());
                        break;
                    case "mvc":
                        structure.addAll(generateIOSMVCStructure());
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported pattern for iOS: " + pattern);
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }

        return structure;
    }

    private List<String> generateAndroidMVVMStructure() {
        return Arrays.asList(
                "app/",
                "app/src/",
                "app/src/main/",
                "app/src/main/java/",
                "app/src/main/res/",
                "app/src/main/res/layout/",
                "app/src/main/res/values/",
                "app/src/main/res/drawable/",
                "app/src/main/AndroidManifest.xml",
                "app/build.gradle",
                "app/proguard-rules.pro",
                "gradle/",
                "gradle/wrapper/",
                "gradle/wrapper/gradle-wrapper.properties",
                "gradle/wrapper/gradle-wrapper.jar",
                "build.gradle",
                "settings.gradle",
                "gradlew",
                "gradlew.bat",
                "local.properties",
                "app/src/main/java/com/example/app/",
                "app/src/main/java/com/example/app/model/",
                "app/src/main/java/com/example/app/viewmodel/",
                "app/src/main/java/com/example/app/view/",
                "app/src/main/java/com/example/app/repository/",
                "app/src/main/java/com/example/app/database/",
                "app/src/main/java/com/example/app/adapter/",
                "app/src/main/java/com/example/app/util/",
                "app/src/main/res/layout/activity_main.xml",
                "app/src/main/res/layout/item_layout.xml",
                "app/src/main/res/layout/dialog_add_item.xml",
                "app/src/main/res/layout/dialog_edit_item.xml",
                "app/src/main/res/values/strings.xml",
                "app/src/main/res/values/colors.xml",
                "app/src/main/res/values/themes.xml",
                "app/src/main/res/values/styles.xml",
                "app/src/main/res/drawable/ic_add.xml",
                "app/src/main/res/drawable/ic_edit.xml",
                "app/src/main/res/drawable/ic_delete.xml"
        );
    }

    private List<String> generateAndroidMVCStructure() {
        return Arrays.asList(
                "app/",
                "app/src/",
                "app/src/main/",
                "app/src/main/java/",
                "app/src/main/res/",
                "app/src/main/res/layout/",
                "app/src/main/res/values/",
                "app/src/main/res/drawable/",
                "app/src/main/AndroidManifest.xml",
                "app/build.gradle",
                "app/proguard-rules.pro",
                "gradle/",
                "gradle/wrapper/",
                "build.gradle",
                "settings.gradle",
                "gradlew",
                "gradlew.bat",
                "app/src/main/java/com/example/app/",
                "app/src/main/java/com/example/app/model/",
                "app/src/main/java/com/example/app/controller/",
                "app/src/main/java/com/example/app/view/",
                "app/src/main/java/com/example/app/database/",
                "app/src/main/java/com/example/app/adapter/",
                "app/src/main/res/layout/activity_main.xml",
                "app/src/main/res/layout/item_layout.xml",
                "app/src/main/res/values/strings.xml",
                "app/src/main/res/values/colors.xml",
                "app/src/main/res/values/themes.xml"
        );
    }

    private List<String> generateIOSMVVMStructure() {
        return Arrays.asList(
                "ProjectName.xcodeproj/",
                "ProjectName/",
                "ProjectName/AppDelegate.swift",
                "ProjectName/SceneDelegate.swift",
                "ProjectName/Models/",
                "ProjectName/ViewModels/",
                "ProjectName/Views/",
                "ProjectName/Services/",
                "ProjectName/Repositories/",
                "ProjectName/Utils/",
                "ProjectName/Resources/",
                "ProjectName/Resources/Assets.xcassets/",
                "ProjectName/Resources/Info.plist",
                "ProjectName/Resources/LaunchScreen.storyboard",
                "ProjectName/Base.lproj/",
                "ProjectName/Base.lproj/Main.storyboard",
                "ProjectName/Models/Item.swift",
                "ProjectName/ViewModels/ItemViewModel.swift",
                "ProjectName/Views/ItemListView.swift",
                "ProjectName/Views/ItemDetailView.swift",
                "ProjectName/Views/AddItemView.swift",
                "ProjectName/Services/DatabaseService.swift",
                "ProjectName/Repositories/ItemRepository.swift",
                "ProjectName/Utils/Constants.swift",
                "ProjectName/Utils/Extensions.swift",
                "ProjectName.xcodeproj/project.pbxproj"
        );
    }

    private List<String> generateIOSMVCStructure() {
        return Arrays.asList(
                "ProjectName.xcodeproj/",
                "ProjectName/",
                "ProjectName/AppDelegate.swift",
                "ProjectName/SceneDelegate.swift",
                "ProjectName/Models/",
                "ProjectName/Controllers/",
                "ProjectName/Views/",
                "ProjectName/Resources/",
                "ProjectName/Utils/",
                "ProjectName/Resources/Assets.xcassets/",
                "ProjectName/Resources/Info.plist",
                "ProjectName/Resources/LaunchScreen.storyboard",
                "ProjectName/Base.lproj/",
                "ProjectName/Base.lproj/Main.storyboard",
                "ProjectName/Models/Item.swift",
                "ProjectName/Controllers/ItemListViewController.swift",
                "ProjectName/Controllers/ItemDetailViewController.swift",
                "ProjectName/Views/ItemCell.swift",
                "ProjectName/Utils/Constants.swift",
                "ProjectName.xcodeproj/project.pbxproj"
        );
    }

    public Map<String, String> getDependencies(String platform, String pattern) {
        Map<String, String> dependencies = new HashMap<>();

        if ("android".equals(platform.toLowerCase())) {
            // Common Android dependencies
            dependencies.put("androidx.appcompat:appcompat", "1.6.1");
            dependencies.put("com.google.android.material:material", "1.9.0");
            dependencies.put("androidx.constraintlayout:constraintlayout", "2.1.4");
            dependencies.put("androidx.recyclerview:recyclerview", "1.3.0");
            dependencies.put("androidx.cardview:cardview", "1.0.0");

            if ("mvvm".equals(pattern.toLowerCase())) {
                // MVVM specific dependencies
                dependencies.put("androidx.lifecycle:lifecycle-viewmodel", "2.6.1");
                dependencies.put("androidx.lifecycle:lifecycle-livedata", "2.6.1");
                dependencies.put("androidx.room:room-runtime", "2.5.2");
                dependencies.put("androidx.room:room-compiler", "2.5.2");
            } else if ("mvc".equals(pattern.toLowerCase())) {
                // MVC specific dependencies
                dependencies.put("androidx.sqlite:sqlite", "2.3.1");
            }

            // Testing dependencies
            dependencies.put("junit:junit", "4.13.2");
            dependencies.put("androidx.test.ext:junit", "1.1.5");
            dependencies.put("androidx.test.espresso:espresso-core", "3.5.1");
        } else if ("ios".equals(platform.toLowerCase())) {
            // iOS dependencies (CocoaPods)
            if ("mvvm".equals(pattern.toLowerCase())) {
                dependencies.put("RxSwift", "6.5.0");
                dependencies.put("RxCocoa", "6.5.0");
                dependencies.put("RealmSwift", "10.42.0");
            } else if ("mvc".equals(pattern.toLowerCase())) {
                dependencies.put("RealmSwift", "10.42.0");
                dependencies.put("SQLite.swift", "0.14.1");
            }
        }

        return dependencies;
    }
}*/
/*package com.codegen.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProjectStructureGenerator {

    public List<String> generateProjectStructure(String platform, String pattern) {
        return switch (platform.toLowerCase()) {
            case "android" -> switch (pattern.toLowerCase()) {
                case "mvvm" -> generateAndroidMVVMStructure();
                case "mvc" -> generateAndroidMVCStructure();
                default -> throw new IllegalArgumentException("Unsupported pattern for Android: " + pattern);
            };
            case "ios" -> switch (pattern.toLowerCase()) {
                case "mvvm" -> generateIOSMVVMStructure();
                case "mvc" -> generateIOSMVCStructure();
                default -> throw new IllegalArgumentException("Unsupported pattern for iOS: " + pattern);
            };
            default -> throw new IllegalArgumentException("Unsupported platform: " + platform);
        };
    }


    public List<String> generateAndroidMVVMStructure() {
        return Arrays.asList(
                "app/",
                "app/src/",
                "app/src/main/",
                "app/src/main/java/",
                "app/src/main/res/",
                "app/src/main/res/layout/",
                "app/src/main/res/values/",
                "app/src/main/res/drawable/",
                "app/src/main/AndroidManifest.xml",
                "app/build.gradle",
                "app/proguard-rules.pro",
                "gradle/",
                "gradle/wrapper/",
                "gradle/wrapper/gradle-wrapper.properties",
                "gradle/wrapper/gradle-wrapper.jar",
                "build.gradle",
                "settings.gradle",
                "gradlew",
                "gradlew.bat",
                "local.properties",
                "app/src/main/java/com/example/app/",
                "app/src/main/java/com/example/app/data/",
                "app/src/main/java/com/example/app/data/local/",
                "app/src/main/java/com/example/app/data/local/dao/",
                "app/src/main/java/com/example/app/data/local/entity/",
                "app/src/main/java/com/example/app/data/remote/",
                "app/src/main/java/com/example/app/data/remote/api/",
                "app/src/main/java/com/example/app/data/remote/dto/",
                "app/src/main/java/com/example/app/data/repository/",
                "app/src/main/java/com/example/app/di/",
                "app/src/main/java/com/example/app/domain/",
                "app/src/main/java/com/example/app/domain/model/",
                "app/src/main/java/com/example/app/domain/repository/",
                "app/src/main/java/com/example/app/domain/usecase/",
                "app/src/main/java/com/example/app/presentation/",
                "app/src/main/java/com/example/app/presentation/ui/",
                "app/src/main/java/com/example/app/presentation/viewmodel/",
                "app/src/main/java/com/example/app/presentation/state/",
                "app/src/main/java/com/example/app/util/",
                "app/src/main/res/layout/activity_main.xml",
                "app/src/main/res/layout/item_layout.xml",
                "app/src/main/res/values/strings.xml",
                "app/src/main/res/values/colors.xml",
                "app/src/main/res/values/themes.xml",
                "app/src/main/res/values/styles.xml",
                "app/src/androidTest/",
                "app/src/test/"
        );
    }

    public List<String> generateAndroidMVCStructure() {
        return Arrays.asList(
                "app/",
                "app/src/",
                "app/src/main/",
                "app/src/main/java/",
                "app/src/main/res/",
                "app/src/main/res/layout/",
                "app/src/main/res/values/",
                "app/src/main/res/drawable/",
                "app/src/main/AndroidManifest.xml",
                "app/build.gradle",
                "app/proguard-rules.pro",
                "gradle/",
                "gradle/wrapper/",
                "build.gradle",
                "settings.gradle",
                "gradlew",
                "gradlew.bat",
                "app/src/main/java/com/example/app/",
                "app/src/main/java/com/example/app/model/",
                "app/src/main/java/com/example/app/controller/",
                "app/src/main/java/com/example/app/view/",
                "app/src/main/java/com/example/app/adapter/",
                "app/src/main/java/com/example/app/util/",
                "app/src/main/res/layout/activity_main.xml",
                "app/src/main/res/layout/item_layout.xml",
                "app/src/main/res/values/strings.xml",
                "app/src/main/res/values/colors.xml",
                "app/src/main/res/values/themes.xml",
                "app/src/androidTest/",
                "app/src/test/"
        );
    }

    public List<String> generateIOSMVVMStructure() {
        return Arrays.asList(
                "ProjectName.xcodeproj/",
                "ProjectName/",
                "ProjectName/App/",
                "ProjectName/App/AppDelegate.swift",
                "ProjectName/App/SceneDelegate.swift",
                "ProjectName/Data/",
                "ProjectName/Data/Local/",
                "ProjectName/Data/Remote/",
                "ProjectName/Data/Repository/",
                "ProjectName/Domain/",
                "ProjectName/Domain/Model/",
                "ProjectName/Domain/UseCase/",
                "ProjectName/Domain/Repository/",
                "ProjectName/Presentation/",
                "ProjectName/Presentation/Scenes/",
                "ProjectName/Presentation/ViewModel/",
                "ProjectName/Core/",
                "ProjectName/Core/Extensions/",
                "ProjectName/Core/Protocols/",
                "ProjectName/Core/Network/",
                "ProjectName/Resources/",
                "ProjectName/Resources/Assets.xcassets/",
                "ProjectName/Resources/Info.plist",
                "ProjectName/Resources/LaunchScreen.storyboard",
                "ProjectName/Resources/Localizable.strings",
                "ProjectNameTests/",
                "ProjectNameUITests/"
        );
    }

    public List<String> generateIOSMVCStructure() {
        return Arrays.asList(
                "ProjectName.xcodeproj/",
                "ProjectName/",
                "ProjectName/AppDelegate.swift",
                "ProjectName/SceneDelegate.swift",
                "ProjectName/Models/",
                "ProjectName/Views/",
                "ProjectName/Controllers/",
                "ProjectName/Services/",
                "ProjectName/Helpers/",
                "ProjectName/Resources/",
                "ProjectName/Resources/Assets.xcassets/",
                "ProjectName/Resources/Info.plist",
                "ProjectName/Resources/LaunchScreen.storyboard",
                "ProjectName/Resources/Localizable.strings",
                "ProjectNameTests/",
                "ProjectNameUITests/"
        );
    }

    public Map<String, String> getDependencies(String platform, String pattern) {
        Map<String, String> dependencies = new HashMap<>();

        switch (platform.toLowerCase()) {
            case "android" -> {
                // Common Android dependencies
                dependencies.put("androidx.recyclerview:recyclerview", "1.3.2");
                dependencies.put("androidx.cardview:cardview", "1.0.0");

                if ("mvvm".equals(pattern.toLowerCase())) {
                    // MVVM specific dependencies
                    dependencies.put("androidx.lifecycle:lifecycle-viewmodel-ktx", "2.7.0");
                    dependencies.put("androidx.lifecycle:lifecycle-livedata-ktx", "2.7.0");
                    dependencies.put("androidx.lifecycle:lifecycle-runtime-ktx", "2.7.0");
                    dependencies.put("androidx.room:room-runtime", "2.6.1");
                    dependencies.put("androidx.room:room-ktx", "2.6.1");
                    dependencies.put("androidx.room:room-compiler", "2.6.1");
                    dependencies.put("com.google.dagger:hilt-android", "2.50");
                    dependencies.put("com.google.dagger:hilt-compiler", "2.50");
                    dependencies.put("org.jetbrains.kotlinx:kotlinx-coroutines-android", "1.7.3");
                    dependencies.put("org.jetbrains.kotlinx:kotlinx-coroutines-core", "1.7.3");
                    dependencies.put("com.squareup.retrofit2:retrofit", "2.9.0");
                    dependencies.put("com.squareup.retrofit2:converter-gson", "2.9.0");
                    dependencies.put("com.squareup.okhttp3:logging-interceptor", "4.12.0");
                } else if ("mvc".equals(pattern.toLowerCase())) {
                    // MVC specific dependencies
                    dependencies.put("androidx.sqlite:sqlite-framework", "2.4.0");
                    dependencies.put("androidx.sqlite:sqlite-ktx", "2.4.0");
                }

                // Testing dependencies
                dependencies.put("junit:junit", "4.13.2");
                dependencies.put("androidx.test.ext:junit", "1.1.5");
                dependencies.put("androidx.test.espresso:espresso-core", "3.5.1");
                dependencies.put("org.mockito:mockito-core", "5.8.0");
                dependencies.put("org.mockito:mockito-android", "5.8.0");
                dependencies.put("io.mockk:mockk", "1.13.8");
            }
            case "ios" -> {
                // iOS dependencies (CocoaPods)
                if ("mvvm".equals(pattern.toLowerCase())) {
                    dependencies.put("RxSwift", "6.6.0");
                    dependencies.put("RxCocoa", "6.6.0");
                    dependencies.put("Alamofire", "5.8.1");
                    dependencies.put("SwiftyJSON", "5.0.1");
                    dependencies.put("Kingfisher", "7.10.1");
                    dependencies.put("SnapKit", "5.6.0");
                    dependencies.put("Resolver", "1.5.0");
                } else if ("mvc".equals(pattern.toLowerCase())) {
                    dependencies.put("Alamofire", "5.8.1");
                    dependencies.put("SwiftyJSON", "5.0.1");
                    dependencies.put("Kingfisher", "7.10.1");
                    dependencies.put("SnapKit", "5.6.0");
                }
            }
        }

        return dependencies;
    }
}*/

package com.codegen.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProjectStructureGenerator {

    public List<String> generateProjectStructure(String platform, String pattern) {
        System.out.println("Platform: " + platform.toLowerCase());
        System.out.println("Pattern: " + pattern.toLowerCase());
        return switch (platform.toLowerCase().trim()) {

            case "android" -> switch (pattern.toLowerCase()) {
                case "mvvm" -> generateAndroidMVVMStructure();
                case "mvc" -> generateAndroidMVCStructure();
                default -> throw new IllegalArgumentException("Unsupported pattern for Android: " + pattern);
            };
            case "ios" -> switch (pattern.toLowerCase()) {
                case "mvc" -> generateIOSMVCStructure();
                case "mvvm" -> generateIOSMVVMStructure();
                default -> throw new IllegalArgumentException("Unsupported pattern for iOS: " + pattern);
            };
            default -> throw new IllegalArgumentException("Unsupported platform: " + platform);
        };
    }

    private List<String> generateAndroidMVVMStructure() {
        return Arrays.asList(
                "app/",
                "app/src/",
                "app/src/main/",
                "app/src/main/java/",
                "app/src/main/res/",
                "app/src/main/res/layout/",
                "app/src/main/res/values/",
                "app/src/main/res/drawable/",
                "app/src/main/AndroidManifest.xml",
                "app/build.gradle",
                "app/proguard-rules.pro",
                "gradle/",
                "gradle/wrapper/",
                "gradle/wrapper/gradle-wrapper.properties",
                "gradle/wrapper/gradle-wrapper.jar",
                "build.gradle",
                "settings.gradle",
                "gradlew",
                "gradlew.bat",
                "local.properties",
                "app/src/main/java/com/example/app/",
                "app/src/main/java/com/example/app/model/",
                "app/src/main/java/com/example/app/viewmodel/",
                "app/src/main/java/com/example/app/view/",
                "app/src/main/java/com/example/app/repository/",
                "app/src/main/java/com/example/app/database/",
                "app/src/main/java/com/example/app/adapter/",
                "app/src/main/java/com/example/app/util/",
                "app/src/main/res/layout/activity_main.xml",
                "app/src/main/res/layout/item_layout.xml",
                "app/src/main/res/layout/dialog_add_item.xml",
                "app/src/main/res/layout/dialog_edit_item.xml",
                "app/src/main/res/values/strings.xml",
                "app/src/main/res/values/colors.xml",
                "app/src/main/res/values/themes.xml",
                "app/src/main/res/values/styles.xml",
                "app/src/main/res/drawable/ic_add.xml",
                "app/src/main/res/drawable/ic_edit.xml",
                "app/src/main/res/drawable/ic_delete.xml"
        );
    }

    private List<String> generateAndroidMVCStructure() {
        return Arrays.asList(
                "app/",
                "app/src/",
                "app/src/main/",
                "app/src/main/java/",
                "app/src/main/res/",
                "app/src/main/res/layout/",
                "app/src/main/res/values/",
                "app/src/main/res/drawable/",
                "app/src/main/AndroidManifest.xml",
                "app/build.gradle",
                "app/proguard-rules.pro",
                "gradle/",
                "gradle/wrapper/",
                "build.gradle",
                "settings.gradle",
                "gradlew",
                "gradlew.bat",
                "app/src/main/java/com/example/app/",
                "app/src/main/java/com/example/app/model/",
                "app/src/main/java/com/example/app/controller/",
                "app/src/main/java/com/example/app/view/",
                "app/src/main/java/com/example/app/database/",
                "app/src/main/java/com/example/app/adapter/",
                "app/src/main/res/layout/activity_main.xml",
                "app/src/main/res/layout/item_layout.xml",
                "app/src/main/res/values/strings.xml",
                "app/src/main/res/values/colors.xml",
                "app/src/main/res/values/themes.xml"
        );
    }

    private List<String> generateIOSMVVMStructure() {
        return Arrays.asList(
                "ProjectName.xcodeproj/",
                "ProjectName/",
                "ProjectName/AppDelegate.swift",
                "ProjectName/SceneDelegate.swift",
                "ProjectName/Models/",
                "ProjectName/ViewModels/",
                "ProjectName/Views/",
                "ProjectName/Services/",
                "ProjectName/Repositories/",
                "ProjectName/Utils/",
                "ProjectName/Resources/",
                "ProjectName/Resources/Assets.xcassets/",
                "ProjectName/Resources/Info.plist",
                "ProjectName/Resources/LaunchScreen.storyboard",
                "ProjectName/Base.lproj/",
                "ProjectName/Base.lproj/Main.storyboard",
                "ProjectName/Models/Item.swift",
                "ProjectName/ViewModels/ItemViewModel.swift",
                "ProjectName/Views/ItemListView.swift",
                "ProjectName/Views/ItemDetailView.swift",
                "ProjectName/Views/AddItemView.swift",
                "ProjectName/Services/DatabaseService.swift",
                "ProjectName/Repositories/ItemRepository.swift",
                "ProjectName/Utils/Constants.swift",
                "ProjectName/Utils/Extensions.swift",
                "ProjectName.xcodeproj/project.pbxproj"
        );
    }

    private List<String> generateIOSMVCStructure() {
        return Arrays.asList(
                "ProjectName.xcodeproj/",
                "ProjectName.xcodeproj/project.pbxproj",
                "ProjectName/",
                "ProjectName/AppDelegate.swift",
                "ProjectName/SceneDelegate.swift",
                "ProjectName/Controllers/",
                "ProjectName/Controllers/ItemListViewController.swift",
                "ProjectName/Controllers/ItemDetailViewController.swift",
                "ProjectName/Controllers/AddItemViewController.swift",
                "ProjectName/Models/",
                "ProjectName/Models/Item.swift",
                "ProjectName/Models/ItemManager.swift",
                "ProjectName/Views/",
                "ProjectName/Views/ItemTableViewCell.swift",
                "ProjectName/Views/ItemDetailView.swift",
                "ProjectName/DataManagers/",
                "ProjectName/DataManagers/DataManager.swift",
                "ProjectName/Resources/",
                "ProjectName/Resources/Assets.xcassets",
                "ProjectName/Resources/LaunchScreen.storyboard",
                "ProjectName/Resources/Info.plist",
                "ProjectName/Utils/",
                "ProjectName/Utils/Constants.swift",
                "ProjectName/Utils/Extensions.swift",
                "ProjectName/Supporting Files/",
                "Podfile"
        );
    }

    public Map<String, String> getDependencies(String platform, String pattern) {
        Map<String, String> dependencies = new HashMap<>();

        if ("android".equals(platform.toLowerCase())) {
            // Common Android dependencies
            dependencies.put("androidx.appcompat:appcompat", "1.6.1");
            dependencies.put("com.google.android.material:material", "1.9.0");
            dependencies.put("androidx.constraintlayout:constraintlayout", "2.1.4");
            dependencies.put("androidx.recyclerview:recyclerview", "1.3.0");
            dependencies.put("androidx.cardview:cardview", "1.0.0");

            if ("mvvm".equals(pattern.toLowerCase())) {
                // MVVM specific dependencies
                dependencies.put("androidx.lifecycle:lifecycle-viewmodel", "2.6.1");
                dependencies.put("androidx.lifecycle:lifecycle-livedata", "2.6.1");
                dependencies.put("androidx.room:room-runtime", "2.5.2");
                dependencies.put("androidx.room:room-compiler", "2.5.2");
            } else if ("mvc".equals(pattern.toLowerCase())) {
                // MVC specific dependencies
                dependencies.put("androidx.sqlite:sqlite", "2.3.1");
            }

            // Testing dependencies
            dependencies.put("junit:junit", "4.13.2");
            dependencies.put("androidx.test.ext:junit", "1.1.5");
            dependencies.put("androidx.test.espresso:espresso-core", "3.5.1");
        } else if ("ios".equals(platform.toLowerCase())) {
            // iOS dependencies (CocoaPods)
            dependencies.put("platform :ios", "'14.0'");
            dependencies.put("use_frameworks!", "");

            if ("mvvm".equals(pattern.toLowerCase())) {
                dependencies.put("pod 'RxSwift'", "'6.5.0'");
                dependencies.put("pod 'RxCocoa'", "'6.5.0'");
                dependencies.put("pod 'SnapKit'", "'5.6.0'");
            } else if ("mvc".equals(pattern.toLowerCase())) {
                dependencies.put("pod 'SnapKit'", "'5.6.0'");
                dependencies.put("pod 'SwiftLint'", "");
                dependencies.put("pod 'KeychainAccess'", "'4.2.2'");
            }
        }

        return dependencies;
    }
}