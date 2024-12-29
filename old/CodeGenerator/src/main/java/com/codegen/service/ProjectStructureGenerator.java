

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
                case "mvp" -> generateAndroidMVPStructure();
                default -> throw new IllegalArgumentException("Unsupported pattern for Android: " + pattern);
            };
            case "ios" -> switch (pattern.toLowerCase()) {
                case "mvc" -> generateIOSMVCStructure();
                case "mvvm" -> generateIOSMVVMStructure();
                case "mvp" -> generateIOSMVPStructure();
                default -> throw new IllegalArgumentException("Unsupported pattern for iOS: " + pattern);
            };
            default -> throw new IllegalArgumentException("Unsupported platform: " + platform);
        };
    }

    private List<String> generateAndroidMVPStructure() {
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
                "app/src/main/java/com/example/app/presenter/",
                "app/src/main/java/com/example/app/view/",
                "app/src/main/java/com/example/app/contract/",
                "app/src/main/java/com/example/app/repository/",
                "app/src/main/java/com/example/app/database/",
                "app/src/main/java/com/example/app/di/",
                "app/src/main/java/com/example/app/util/",
                "app/src/main/res/layout/activity_main.xml",
                "app/src/main/res/layout/fragment_main.xml",
                "app/src/main/res/layout/item_layout.xml",
                "app/src/main/res/values/strings.xml",
                "app/src/main/res/values/colors.xml",
                "app/src/main/res/values/themes.xml",
                "app/src/main/res/values/styles.xml",
                "app/src/test/java/com/example/app/presenter/",
                "app/src/androidTest/java/com/example/app/"
        );
    }

    private List<String> generateIOSMVPStructure() {
        return Arrays.asList(
                "ProjectName.xcodeproj/",
                "ProjectName/",
                "ProjectName/AppDelegate.swift",
                "ProjectName/SceneDelegate.swift",
                "ProjectName/Models/",
                "ProjectName/Models/Item.swift",
                "ProjectName/Models/DataManager.swift",
                "ProjectName/Presenters/",
                "ProjectName/Presenters/ItemListPresenter.swift",
                "ProjectName/Presenters/ItemDetailPresenter.swift",
                "ProjectName/Views/",
                "ProjectName/Views/Protocols/",
                "ProjectName/Views/Protocols/ItemListViewProtocol.swift",
                "ProjectName/Views/Protocols/ItemDetailViewProtocol.swift",
                "ProjectName/Views/ViewControllers/",
                "ProjectName/Views/ViewControllers/ItemListViewController.swift",
                "ProjectName/Views/ViewControllers/ItemDetailViewController.swift",
                "ProjectName/Views/CustomViews/",
                "ProjectName/Views/CustomViews/ItemCell.swift",
                "ProjectName/Services/",
                "ProjectName/Services/NetworkService.swift",
                "ProjectName/Services/StorageService.swift",
                "ProjectName/Protocols/",
                "ProjectName/Protocols/PresenterProtocols.swift",
                "ProjectName/Protocols/ServiceProtocols.swift",
                "ProjectName/Resources/",
                "ProjectName/Resources/Assets.xcassets/",
                "ProjectName/Resources/Info.plist",
                "ProjectName/Resources/LaunchScreen.storyboard",
                "ProjectName/Utils/",
                "ProjectName/Utils/Constants.swift",
                "ProjectName/Utils/Extensions.swift",
                "ProjectNameTests/",
                "ProjectNameTests/Presenters/",
                "ProjectNameTests/Mocks/",
                "Podfile"
        );
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

            switch (pattern.toLowerCase()) {
                case "mvvm" -> {
                    dependencies.put("androidx.lifecycle:lifecycle-viewmodel", "2.6.1");
                    dependencies.put("androidx.lifecycle:lifecycle-livedata", "2.6.1");
                    dependencies.put("androidx.room:room-runtime", "2.5.2");
                    dependencies.put("androidx.room:room-compiler", "2.5.2");
                }
                case "mvc" -> dependencies.put("androidx.sqlite:sqlite", "2.3.1");
                case "mvp" -> {
                    dependencies.put("com.google.dagger:dagger", "2.48");
                    dependencies.put("com.google.dagger:dagger-compiler", "2.48");
                    dependencies.put("io.reactivex.rxjava3:rxjava", "3.1.6");
                    dependencies.put("io.reactivex.rxjava3:rxandroid", "3.0.2");
                }
            }

            // Testing dependencies
            dependencies.put("junit:junit", "4.13.2");
            dependencies.put("org.mockito:mockito-core", "5.3.1");
            dependencies.put("androidx.test.ext:junit", "1.1.5");
            dependencies.put("androidx.test.espresso:espresso-core", "3.5.1");
        } else if ("ios".equals(platform.toLowerCase())) {
            // iOS dependencies (CocoaPods)
            dependencies.put("platform :ios", "'14.0'");
            dependencies.put("use_frameworks!", "");

            switch (pattern.toLowerCase()) {
                case "mvvm" -> {
                    dependencies.put("pod 'RxSwift'", "'6.5.0'");
                    dependencies.put("pod 'RxCocoa'", "'6.5.0'");
                    dependencies.put("pod 'SnapKit'", "'5.6.0'");
                }
                case "mvc" -> {
                    dependencies.put("pod 'SnapKit'", "'5.6.0'");
                    dependencies.put("pod 'SwiftLint'", "");
                    dependencies.put("pod 'KeychainAccess'", "'4.2.2'");
                }
                case "mvp" -> {
                    dependencies.put("pod 'SnapKit'", "'5.6.0'");
                    dependencies.put("pod 'Swinject'", "'2.8.3'");
                    dependencies.put("pod 'Quick'", "'7.0.0'");
                    dependencies.put("pod 'Nimble'", "'12.0.0'");
                }
            }
        }

        return dependencies;
    }
}