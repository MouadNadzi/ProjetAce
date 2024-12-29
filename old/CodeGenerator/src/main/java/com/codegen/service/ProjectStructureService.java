package com.codegen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProjectStructureService {
    private final CodeGeneratorService codeGeneratorService;

    @Autowired
    public ProjectStructureService(CodeGeneratorService codeGeneratorService) {
        this.codeGeneratorService = codeGeneratorService;
    }


    /*public Map<String, String> generateAndroidMVVMStructure(String packageName, String projectName) {
        Map<String, String> files = new HashMap<>();
        String packagePath = packageName.replace('.', '/');

        // Add app-level build.gradle
        files.put("app/build.gradle", generateAndroidBuildGradle(packageName));

        // Add project-level build.gradle
        files.put("build.gradle", generateProjectBuildGradle());

        // Add settings.gradle
        files.put("settings.gradle", generateSettingsGradle(projectName));

        // Add gradle properties
        files.put("gradle.properties", generateGradleProperties());

        // Add AndroidManifest.xml
        files.put("app/src/main/AndroidManifest.xml", generateAndroidManifest(packageName, projectName));

        // Add Java source files
        String javaPath = "app/src/main/java/" + packagePath + "/";
        files.put(javaPath + projectName + "Activity.java", generateActivity(packageName, projectName));
        files.put(javaPath + projectName + "ViewModel.java", generateViewModel(packageName, projectName));
        files.put(javaPath + projectName + "Model.java", generateModel(packageName, projectName));
        files.put(javaPath + projectName + "Repository.java", generateRepository(packageName, projectName));
        files.put(javaPath + projectName + "Database.java", generateDatabase(packageName, projectName));
        files.put(javaPath + projectName + "Dao.java", generateDao(packageName, projectName));
        files.put(javaPath + projectName + "Adapter.java", generateAdapter(packageName, projectName));

        // Add layouts
        files.put("app/src/main/res/layout/activity_main.xml", generateMainActivityLayout(projectName));
        files.put("app/src/main/res/layout/item_layout.xml", generateItemLayout());
        files.put("app/src/main/res/layout/dialog_add_item.xml", generateAddItemDialogLayout());
        files.put("app/src/main/res/layout/dialog_edit_item.xml", generateEditItemDialogLayout());

        // Add values resources
        files.put("app/src/main/res/values/strings.xml", generateStringsXml(projectName));
        files.put("app/src/main/res/values/colors.xml", generateColorsXml());
        files.put("app/src/main/res/values/themes.xml", generateThemesXml(projectName));
        files.put("app/src/main/res/values/styles.xml", generateStylesXml());

        return files;
    }*/
    public Map<String, String> generateBaseStructure(
            String platform,
            String pattern,
            String packageName,
            String projectName) {

        Map<String, String> files = new HashMap<>();

        if ("android".equalsIgnoreCase(platform)) {
            // Add Android-specific base files
            files.put("app/build.gradle", generateAndroidBuildGradle(packageName));
            files.put("build.gradle", generateProjectBuildGradle());
            files.put("settings.gradle", generateSettingsGradle(projectName));
            files.put("gradle.properties", generateGradleProperties());
            files.put("app/src/main/AndroidManifest.xml",
                    generateAndroidManifest(packageName, projectName));
        } else if ("ios".equalsIgnoreCase(platform)) {
            // Add iOS-specific base files
            files.put(projectName + ".xcodeproj/project.pbxproj",
                    generateXcodeProjFile(projectName));
            files.put(projectName + "/Info.plist", generateInfoPlist(projectName));
            files.put(projectName + "/AppDelegate.swift", generateAppDelegate(projectName));
            files.put(projectName + "/SceneDelegate.swift", generateSceneDelegate(projectName));
        }

        return files;
    }

    private String generateActivity(String packageName, String projectName) {
        return """
        package %s;
        
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.lifecycle.ViewModelProvider;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        
        public class %sActivity extends AppCompatActivity {
            private %sViewModel viewModel;
            private %sAdapter adapter;
            
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                
                setupViewModel();
                setupRecyclerView();
                setupFab();
            }
            
            private void setupViewModel() {
                viewModel = new ViewModelProvider(this).get(%sViewModel.class);
                viewModel.getAllItems().observe(this, items -> {
                    adapter.submitList(items);
                });
            }
            
            private void setupRecyclerView() {
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                adapter = new %sAdapter(item -> {
                    // Handle item click
                    viewModel.select(item);
                });
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
            
            private void setupFab() {
                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setOnClickListener(view -> {
                    // Show add dialog
                    showAddItemDialog();
                });
            }
            
            private void showAddItemDialog() {
                // Implementation for add dialog
            }
        }
        """.formatted(packageName, projectName, projectName, projectName, projectName, projectName);
    }

    private String generateViewModel(String packageName, String projectName) {
        return """
        package %s;
        
        import android.app.Application;
        import androidx.lifecycle.AndroidViewModel;
        import androidx.lifecycle.LiveData;
        import java.util.List;
        
        public class %sViewModel extends AndroidViewModel {
            private %sRepository repository;
            private LiveData<List<%sModel>> allItems;
            
            public %sViewModel(Application application) {
                super(application);
                repository = new %sRepository(application);
                allItems = repository.getAllItems();
            }
            
            public LiveData<List<%sModel>> getAllItems() {
                return allItems;
            }
            
            public void insert(%sModel item) {
                repository.insert(item);
            }
            
            public void update(%sModel item) {
                repository.update(item);
            }
            
            public void delete(%sModel item) {
                repository.delete(item);
            }
        }
        """.formatted(packageName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName);
    }

    private String generateModel(String packageName, String projectName) {
        return """
        package %s;
        
        import androidx.room.Entity;
        import androidx.room.PrimaryKey;
        
        @Entity(tableName = "items")
        public class %sModel {
            @PrimaryKey(autoGenerate = true)
            private int id;
            private String title;
            private String description;
            private boolean completed;
            
            public %sModel(String title, String description) {
                this.title = title;
                this.description = description;
                this.completed = false;
            }
            
            // Getters and setters
            public int getId() { return id; }
            public void setId(int id) { this.id = id; }
            
            public String getTitle() { return title; }
            public void setTitle(String title) { this.title = title; }
            
            public String getDescription() { return description; }
            public void setDescription(String description) { this.description = description; }
            
            public boolean isCompleted() { return completed; }
            public void setCompleted(boolean completed) { this.completed = completed; }
        }
        """.formatted(packageName, projectName, projectName);
    }

    private String generateRepository(String packageName, String projectName) {
        return """
        package %s;
        
        import android.app.Application;
        import androidx.lifecycle.LiveData;
        import java.util.List;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        
        public class %sRepository {
            private %sDao itemDao;
            private LiveData<List<%sModel>> allItems;
            private ExecutorService executorService;
            
            public %sRepository(Application application) {
                %sDatabase database = %sDatabase.getDatabase(application);
                itemDao = database.itemDao();
                allItems = itemDao.getAllItems();
                executorService = Executors.newSingleThreadExecutor();
            }
            
            public LiveData<List<%sModel>> getAllItems() {
                return allItems;
            }
            
            public void insert(%sModel item) {
                executorService.execute(() -> itemDao.insert(item));
            }
            
            public void update(%sModel item) {
                executorService.execute(() -> itemDao.update(item));
            }
            
            public void delete(%sModel item) {
                executorService.execute(() -> itemDao.delete(item));
            }
        }
        """.formatted(packageName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName, projectName);
    }

    private String generateDatabase(String packageName, String projectName) {
        return """
        package %s;
        
        import android.content.Context;
        import androidx.room.Database;
        import androidx.room.Room;
        import androidx.room.RoomDatabase;
        
        @Database(entities = {%sModel.class}, version = 1)
        public abstract class %sDatabase extends RoomDatabase {
            public abstract %sDao itemDao();
            
            private static volatile %sDatabase INSTANCE;
            
            static %sDatabase getDatabase(final Context context) {
                if (INSTANCE == null) {
                    synchronized (%sDatabase.class) {
                        if (INSTANCE == null) {
                            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    %sDatabase.class, "item_database")
                                    .build();
                        }
                    }
                }
                return INSTANCE;
            }
        }
        """.formatted(packageName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName);
    }

    private String generateDao(String packageName, String projectName) {
        return """
        package %s;
        
        import androidx.lifecycle.LiveData;
        import androidx.room.*;
        import java.util.List;
        
        @Dao
        public interface %sDao {
            @Query("SELECT * FROM items ORDER BY id ASC")
            LiveData<List<%sModel>> getAllItems();
            
            @Insert
            void insert(%sModel item);
            
            @Update
            void update(%sModel item);
            
            @Delete
            void delete(%sModel item);
        }
        """.formatted(packageName, projectName, projectName, projectName, projectName, projectName);
    }

    private String generateAdapter(String packageName, String projectName) {
        return """
        package %s;
        
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import androidx.recyclerview.widget.RecyclerView;
        import androidx.recyclerview.widget.ListAdapter;
        import androidx.recyclerview.widget.DiffUtil;
        
        public class %sAdapter extends ListAdapter<%sModel, %sAdapter.ViewHolder> {
            private OnItemClickListener listener;
            
            public interface OnItemClickListener {
                void onItemClick(%sModel item);
            }
            
            protected %sAdapter(OnItemClickListener listener) {
                super(DIFF_CALLBACK);
                this.listener = listener;
            }
            
            private static final DiffUtil.ItemCallback<%sModel> DIFF_CALLBACK =
                    new DiffUtil.ItemCallback<%sModel>() {
                @Override
                public boolean areItemsTheSame(%sModel oldItem, %sModel newItem) {
                    return oldItem.getId() == newItem.getId();
                }
                
                @Override
                public boolean areContentsTheSame(%sModel oldItem, %sModel newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle()) &&
                           oldItem.getDescription().equals(newItem.getDescription()) &&
                           oldItem.isCompleted() == newItem.isCompleted();
                }
            };
            
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_layout, parent, false);
                return new ViewHolder(view);
            }
            
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                %sModel current = getItem(position);
                holder.bind(current, listener);
            }
            
            static class ViewHolder extends RecyclerView.ViewHolder {
                private TextView titleView;
                private TextView descriptionView;
                
                ViewHolder(View itemView) {
                    super(itemView);
                    titleView = itemView.findViewById(R.id.title);
                    descriptionView = itemView.findViewById(R.id.description);
                }
                
                void bind(%sModel item, OnItemClickListener listener) {
                    titleView.setText(item.getTitle());
                    descriptionView.setText(item.getDescription());
                    itemView.setOnClickListener(v -> listener.onItemClick(item));
                }
            }
        }
        """.formatted(packageName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName);
    }

    private String generateAndroidBuildGradle(String packageName) {
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
                
                testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
            }
            
            buildTypes {
                release {
                    minifyEnabled false
                    proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
                }
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
            
            // ViewModel and LiveData
            implementation 'androidx.lifecycle:lifecycle-viewmodel:2.6.1'
            implementation 'androidx.lifecycle:lifecycle-livedata:2.6.1'
            
            // Room components
            implementation 'androidx.room:room-runtime:2.5.2'
            annotationProcessor 'androidx.room:room-compiler:2.5.2'
            
            // Testing
            testImplementation 'junit:junit:4.13.2'
            androidTestImplementation 'androidx.test.ext:junit:1.1.5'
            androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
        }
        """.formatted(packageName, packageName);
    }
    /*private String generateAndroidBuildGradle(String packageName) {
        return """
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "${packageName}"
    compileSdk = 34

    defaultConfig {
        applicationId = "${packageName}"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
""".formatted(packageName, packageName);
    }*/

    private String generateProjectBuildGradle() {
        return """
        buildscript {
            repositories {
                google()
                mavenCentral()
            }
            dependencies {
                classpath 'com.android.tools.build:gradle:8.1.0'
            }
        }
        
        // Remove the allprojects block to avoid conflicts with settings.gradle
        // allprojects {
        //     repositories {
        //         google()
        //         mavenCentral()
        //     }
        // }

        tasks.register('clean', Delete) {
            delete rootProject.buildDir
        }
        """;
    }


    private String generateSettingsGradle(String projectName) {
        return """
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
        """.formatted(projectName);
    }

    private String generateGradleProperties() {
        return """
        org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
        android.useAndroidX=true
        android.enableJetifier=true
        android.nonTransitiveRClass=true
        """;
    }

    private String generateAndroidManifest(String packageName, String projectName) {
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
                android:theme="@style/Theme.%s">
                
                <activity
                    android:name=".%sActivity"
                    android:exported="true">
                    <intent-filter>
                        <action android:name="android.intent.action.MAIN" />
                        <category android:name="android.intent.category.LAUNCHER" />
                    </intent-filter>
                </activity>
            </application>
        </manifest>
        """.formatted(packageName, projectName, projectName);
    }

    private String generateMainActivityLayout(String projectName) {
        return """
        <?xml version="1.0" encoding="utf-8"?>
        <androidx.coordinatorlayout.widget.CoordinatorLayout 
            xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <com.google.android.material.appbar.AppBarLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content">

                <com.google.android.material.appbar.MaterialToolbar
                    android:id="@+id/toolbar"
                    android:layout_width="match_parent"
                    android:layout_height="?attr/actionBarSize"
                    android:background="?attr/colorPrimary"
                    app:title="@string/app_name"
                    app:titleTextColor="@android:color/white"/>
            </com.google.android.material.appbar.AppBarLayout>

            <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/recyclerView"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                app:layout_behavior="@string/appbar_scrolling_view_behavior"
                android:padding="8dp"
                android:clipToPadding="false"/>

            <com.google.android.material.floatingactionbutton.FloatingActionButton
                android:id="@+id/fab"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="bottom|end"
                android:layout_margin="16dp"
                android:contentDescription="@string/add_item"
                app:srcCompat="@android:drawable/ic_input_add"/>

        </androidx.coordinatorlayout.widget.CoordinatorLayout>
        """;
    }

    private String generateMainActivityLayout() {
        return """
        <?xml version="1.0" encoding="utf-8"?>
        <androidx.coordinatorlayout.widget.CoordinatorLayout
            xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
            
            <com.google.android.material.appbar.AppBarLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content">
                
                <com.google.android.material.appbar.MaterialToolbar
                    android:id="@+id/toolbar"
                    android:layout_width="match_parent"
                    android:layout_height="?attr/actionBarSize"
                    android:background="?attr/colorPrimary"
                    app:title="@string/app_name"
                    app:titleTextColor="@android:color/white"/>
                    
            </com.google.android.material.appbar.AppBarLayout>
            
            <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/recyclerView"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                app:layout_behavior="@string/appbar_scrolling_view_behavior"
                android:padding="16dp"
                android:clipToPadding="false"/>
                
            <com.google.android.material.floatingactionbutton.FloatingActionButton
                android:id="@+id/fab"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="bottom|end"
                android:layout_margin="16dp"
                app:srcCompat="@android:drawable/ic_input_add"/>
                
        </androidx.coordinatorlayout.widget.CoordinatorLayout>
        """;
    }

    private String generateItemLayout() {
        return """
        <?xml version="1.0" encoding="utf-8"?>
        <com.google.android.material.card.MaterialCardView
            xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="8dp"
            android:layout_marginVertical="4dp"
            app:cardElevation="2dp"
            app:cardCornerRadius="8dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="16dp">

                <TextView
                    android:id="@+id/titleText"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:textSize="18sp"
                    android:textColor="?android:attr/textColorPrimary"
                    android:textStyle="bold"/>

                <TextView
                    android:id="@+id/descriptionText"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="8dp"
                    android:textColor="?android:attr/textColorSecondary"/>

                <com.google.android.material.checkbox.MaterialCheckBox
                    android:id="@+id/completedCheckbox"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="8dp"
                    android:text="@string/mark_completed"/>

            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>
        """;
    }

    private String generateAddItemDialogLayout() {
        return """
        <?xml version="1.0" encoding="utf-8"?>
        <LinearLayout
            xmlns:android="http://schemas.android.com/apk/res/android"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="16dp">

            <com.google.android.material.textfield.TextInputLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="@string/title"
                style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/titleInput"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:inputType="text"
                    android:maxLines="1"/>

            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:hint="@string/description"
                style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/descriptionInput"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:inputType="textMultiLine"
                    android:minLines="3"/>

            </com.google.android.material.textfield.TextInputLayout>

        </LinearLayout>
        """;
    }

    private String generateEditItemDialogLayout() {
        return """
        <?xml version="1.0" encoding="utf-8"?>
        <LinearLayout
            xmlns:android="http://schemas.android.com/apk/res/android"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="16dp">

            <com.google.android.material.textfield.TextInputLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="@string/title"
                style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/titleInput"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:inputType="text"
                    android:maxLines="1"/>

            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:hint="@string/description"
                style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/descriptionInput"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:inputType="textMultiLine"
                    android:minLines="3"/>

            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.checkbox.MaterialCheckBox
                android:id="@+id/completedCheckbox"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:text="@string/mark_completed"/>

        </LinearLayout>
        """;
    }

    private String generateStringsXml(String projectName) {
        return """
        <?xml version="1.0" encoding="utf-8"?>
        <resources>
            <string name="app_name">%s</string>
            <string name="add_item">Add Item</string>
            <string name="edit_item">Edit Item</string>
            <string name="delete_item">Delete Item</string>
            <string name="title">Title</string>
            <string name="description">Description</string>
            <string name="mark_completed">Mark as completed</string>
            <string name="save">Save</string>
            <string name="cancel">Cancel</string>
            <string name="delete">Delete</string>
            <string name="undo">UNDO</string>
            <string name="item_deleted">Item deleted</string>
            <string name="search">Search</string>
            <string name="no_items">No items yet</string>
            <string name="error_title_required">Title is required</string>
            <string name="delete_confirmation">Are you sure you want to delete this item?</string>
        </resources>
        """.formatted(projectName);
    }

    private String generateColorsXml() {
        return """
        <?xml version="1.0" encoding="utf-8"?>
        <resources>
            <color name="primary">#6200EE</color>
            <color name="primaryDark">#3700B3</color>
            <color name="primaryVariant">#3700B3</color>
            <color name="secondary">#03DAC6</color>
            <color name="secondaryVariant">#018786</color>
            <color name="background">#FFFFFF</color>
            <color name="surface">#FFFFFF</color>
            <color name="error">#B00020</color>
            <color name="onPrimary">#FFFFFF</color>
            <color name="onSecondary">#000000</color>
            <color name="onBackground">#000000</color>
            <color name="onSurface">#000000</color>
            <color name="onError">#FFFFFF</color>
        </resources>
        """;
    }

    private String generateThemesXml(String projectName) {
        return """
        <?xml version="1.0" encoding="utf-8"?>
        <resources>
            <style name="Theme.%s" parent="Theme.MaterialComponents.DayNight.NoActionBar">
                <item name="colorPrimary">@color/primary</item>
                <item name="colorPrimaryDark">@color/primaryDark</item>
                <item name="colorPrimaryVariant">@color/primaryVariant</item>
                <item name="colorSecondary">@color/secondary</item>
                <item name="colorSecondaryVariant">@color/secondaryVariant</item>
                <item name="android:colorBackground">@color/background</item>
                <item name="colorSurface">@color/surface</item>
                <item name="colorError">@color/error</item>
                <item name="colorOnPrimary">@color/onPrimary</item>
                <item name="colorOnSecondary">@color/onSecondary</item>
                <item name="colorOnBackground">@color/onBackground</item>
                <item name="colorOnSurface">@color/onSurface</item>
                <item name="colorOnError">@color/onError</item>
                <item name="android:statusBarColor">?attr/colorPrimaryVariant</item>
            </style>
        </resources>
        """.formatted(projectName);
    }

    private String generateStylesXml() {
        return """
        <?xml version="1.0" encoding="utf-8"?>
        <resources>
            <style name="TextAppearance.App.Headline1" parent="TextAppearance.MaterialComponents.Headline1">
                <item name="android:textSize">24sp</item>
                <item name="android:textStyle">bold</item>
            </style>
            
            <style name="TextAppearance.App.Headline2" parent="TextAppearance.MaterialComponents.Headline2">
                <item name="android:textSize">20sp</item>
                <item name="android:textStyle">bold</item>
            </style>
            
            <style name="Widget.App.Button" parent="Widget.MaterialComponents.Button">
                <item name="android:textAllCaps">false</item>
                <item name="android:padding">16dp</item>
            </style>
            
            <style name="Widget.App.CardView" parent="Widget.MaterialComponents.CardView">
                <item name="cardElevation">4dp</item>
                <item name="cardCornerRadius">8dp</item>
                <item name="android:layout_margin">8dp</item>
            </style>
        </resources>
        """;
    }


    /*public Map<String, String> generateAndroidMVCStructure(String packageName, String projectName) {
        Map<String, String> files = new HashMap<>();
        String packagePath = packageName.replace('.', '/');

        // Add app-level build.gradle
        files.put("app/build.gradle", generateAndroidBuildGradle(packageName));

        // Add project-level build.gradle
        files.put("build.gradle", generateProjectBuildGradle());

        // Add settings.gradle
        files.put("settings.gradle", generateSettingsGradle(projectName));

        // Add gradle properties
        files.put("gradle.properties", generateGradleProperties());

        // Add AndroidManifest.xml
        files.put("app/src/main/AndroidManifest.xml", generateAndroidManifest(packageName, projectName));

        // Add MVC-specific files
        files.put(getSourcePath(packagePath, "controller/" + projectName + "Controller.java"),
                codeGeneratorService.generateMVCController(packageName, projectName));

        files.put(getSourcePath(packagePath, "controller/callbacks/" + projectName + "Callbacks.java"),
                codeGeneratorService.generateMVCCallbacks(packageName, projectName));

        files.put(getSourcePath(packagePath, "model/database/" + projectName + "Dao.java"),
                codeGeneratorService.generateMVCDao(packageName, projectName));

        files.put(getSourcePath(packagePath, "model/database/DateConverter.java"),
                codeGeneratorService.generateDateConverter(packageName));

        // Add layouts
        files.put("app/src/main/res/layout/activity_main.xml",
                codeGeneratorService.generateMVCLayout(projectName));
        files.put("app/src/main/res/layout/item_layout.xml",
                codeGeneratorService.generateMVCItemLayout());

        // Add values resources
        files.put("app/src/main/res/values/strings.xml", generateStringsXml(projectName));
        files.put("app/src/main/res/values/colors.xml", generateColorsXml());
        files.put("app/src/main/res/values/themes.xml", generateThemesXml(projectName));
        files.put("app/src/main/res/values/styles.xml", generateStylesXml());

        return files;
    }*/

    private void addCommonResources(Map<String, String> files, String projectName) {
        // Add resource files
        files.put("app/src/main/res/values/strings.xml", generateStringsXml(projectName));
        files.put("app/src/main/res/values/colors.xml", generateColorsXml());
        files.put("app/src/main/res/values/themes.xml", generateThemesXml(projectName));
        files.put("app/src/main/res/values/styles.xml", generateStylesXml());
    }
    private String getSourcePath(String packagePath, String fileName) {
        return "app/src/main/java/" + packagePath + "/" + fileName;
    }
    /*public Map<String, String> generateProjectStructure(String platform, String pattern, String packageName, String projectName) {
        switch (platform.toLowerCase()) {
            case "android":
                return switch (pattern.toLowerCase()) {
                    case "mvc" -> generateAndroidMVCStructure(packageName, projectName);
                    case "mvvm" -> generateAndroidMVVMStructure(packageName, projectName);
                    default -> throw new UnsupportedOperationException("Unsupported pattern: " + pattern);
                };
            case "ios":
                return switch (pattern.toLowerCase()) {
                    case "mvc" -> generateIOSMVCStructure(packageName, projectName);
                    case "mvvm" -> generateIOSMVVMStructure(packageName, projectName);
                    default -> throw new UnsupportedOperationException("Unsupported pattern: " + pattern);
                };
            default:
                throw new UnsupportedOperationException("Unsupported platform: " + platform);
        }
    }*/
    /*public Map<String, String> generateProjectStructure(
            String packageName,
            String projectName,
            String platform,
            String pattern) {
        if ("android".equals(platform.toLowerCase())) {
            return switch (pattern.toLowerCase()) {
                case "mvvm" -> generateAndroidMVVMStructure(packageName, projectName);
                case "mvc" -> generateAndroidMVCStructure(packageName, projectName);
                default -> throw new UnsupportedOperationException("Unsupported pattern: " + pattern);
            };
        }
        throw new UnsupportedOperationException("Unsupported platform: " + platform);
    }*/
    public Map<String, String> generateProjectStructure(
            String packageName,
            String projectName,
            String platform,
            String pattern) {
        switch (platform.toLowerCase()) {
            case "android" -> {
                // Android MVVM / MVC
                return switch (pattern.toLowerCase()) {
                    case "mvvm" -> generateAndroidMVVMStructure(packageName, projectName);
                    case "mvc" -> generateAndroidMVCStructure(packageName, projectName);
                    default -> throw new UnsupportedOperationException("Unsupported pattern: " + pattern);
                };
            }
            case "ios" -> {
                // iOS MVVM / MVC
                return switch (pattern.toLowerCase()) {
                    case "mvc" -> generateIOSMVCStructure(packageName, projectName);
                    case "mvvm" -> generateIOSMVVMStructure(packageName, projectName);
                    default -> throw new UnsupportedOperationException("Unsupported pattern: " + pattern);
                };
            }
            default -> throw new UnsupportedOperationException("Unsupported platform: " + platform);
        }
    }



    private Map<String, String> generateAndroidMVVMStructure(String packageName, String projectName) {
        Map<String, String> files = new HashMap<>();
        String packagePath = packageName.replace('.', '/');

        // Add app-level build.gradle
        files.put("app/build.gradle", generateAndroidBuildGradle(packageName));

        // Add project-level build.gradle
        files.put("build.gradle", generateProjectBuildGradle());

        // Add settings.gradle
        files.put("settings.gradle", generateSettingsGradle(projectName));

        // Add gradle properties
        files.put("gradle.properties", generateGradleProperties());

        // Add AndroidManifest.xml
        files.put("app/src/main/AndroidManifest.xml", generateAndroidManifest(packageName, projectName));

        // Add Java source files
        String javaPath = "app/src/main/java/" + packagePath + "/";
        files.put(javaPath + projectName + "Activity.java",
                codeGeneratorService.generateMVVMActivity(packageName, projectName));
        files.put(javaPath + projectName + "ViewModel.java",
                codeGeneratorService.generateMVVMViewModel(packageName, projectName));
        files.put(javaPath + projectName + "Model.java",
                codeGeneratorService.generateMVVMModel(packageName, projectName));
        files.put(javaPath + projectName + "Repository.java",
                codeGeneratorService.generateMVVMRepository(packageName, projectName));

        // Add common resources
        addCommonResources(files, projectName);

        return files;
    }

    private Map<String, String> generateAndroidMVCStructure(String packageName, String projectName) {
        Map<String, String> files = new HashMap<>();
        String packagePath = packageName.replace('.', '/');

        // Add configuration files
        files.put("app/build.gradle", generateAndroidBuildGradle(packageName));
        files.put("build.gradle", generateProjectBuildGradle());
        files.put("settings.gradle", generateSettingsGradle(projectName));
        files.put("gradle.properties", generateGradleProperties());
        files.put("app/src/main/AndroidManifest.xml", generateAndroidManifest(packageName, projectName));

        // Add MVC-specific files
        files.put(getSourcePath(packagePath, "controller/" + projectName + "Controller.java"),
                codeGeneratorService.generateMVCController(packageName, projectName));

        files.put(getSourcePath(packagePath, "controller/callbacks/" + projectName + "Callbacks.java"),
                codeGeneratorService.generateMVCCallbacks(packageName, projectName));

        files.put(getSourcePath(packagePath, "model/database/" + projectName + "Dao.java"),
                codeGeneratorService.generateMVCDao(packageName, projectName));

        files.put(getSourcePath(packagePath, "model/database/DateConverter.java"),
                codeGeneratorService.generateDateConverter(packageName));

        // Add layouts
        files.put("app/src/main/res/layout/activity_main.xml",
                codeGeneratorService.generateMVCLayout(projectName));
        files.put("app/src/main/res/layout/item_layout.xml",
                codeGeneratorService.generateMVCItemLayout());

        // Add common resources
        addCommonResources(files, projectName);

        return files;
    }


    /*public Map<String, String> generateAndroidMVCStructure(String packageName, String projectName) {
        Map<String, String> files = new HashMap<>();
        // TODO: Implement iOS MVC structure generation
        throw new UnsupportedOperationException("iOS MVC structure generation not yet implemented");
        // return files;
    }*/




    /*public Map<String, String> generateIOSMVCStructure(String packageName, String projectName) {
        Map<String, String> files = new HashMap<>();
        // TODO: Implement iOS MVC structure generation
        throw new UnsupportedOperationException("iOS MVC structure generation not yet implemented");
        // return files;
    }*/
    private Map<String, String> generateIOSMVCStructure(String packageName, String projectName) {
        Map<String, String> files = new HashMap<>();

        // Add source files
        files.put(projectName + "/Controllers/" + projectName + "ViewController.swift",
                codeGeneratorService.generateIOSMVCViewController(projectName));

        files.put(projectName + "/Models/" + projectName + "Model.swift",
                codeGeneratorService.generateIOSMVCModel(projectName));

        files.put(projectName + "/DataManagers/" + projectName + "DataManager.swift",
                codeGeneratorService.generateIOSMVCDataManager(projectName));

        files.put(projectName + "/Views/" + projectName + "TableViewCell.swift",
                codeGeneratorService.generateIOSMVCTableViewCell(projectName));

        // Add project configuration files
        files.put(projectName + ".xcodeproj/project.pbxproj", generateXcodeProjFile(projectName));
        files.put(projectName + "/Info.plist", generateInfoPlist(projectName));
        files.put(projectName + "/SceneDelegate.swift", generateSceneDelegate(projectName));
        files.put(projectName + "/AppDelegate.swift", generateAppDelegate(projectName));

        return files;
    }

    private Map<String, String> generateIOSMVCStructure(String packageName, String projectName, ParsedUMLModel.UMLClass umlClass) {
        Map<String, String> files = new HashMap<>();

        // Add source files
        files.put(projectName + "/Controllers/" + projectName + "ViewController.swift",
                codeGeneratorService.generateIOSMVCViewController(projectName));

        // Pass the umlClass to generateIOSMVCModel
        files.put(projectName + "/Models/" + projectName + "Model.swift",
                codeGeneratorService.generateIOSMVCModel(umlClass, projectName, packageName));

        files.put(projectName + "/DataManagers/" + projectName + "DataManager.swift",
                codeGeneratorService.generateIOSMVCDataManager(projectName));

        files.put(projectName + "/Views/" + projectName + "TableViewCell.swift",
                codeGeneratorService.generateIOSMVCTableViewCell(projectName));

        // Add project configuration files
        files.put(projectName + ".xcodeproj/project.pbxproj", generateXcodeProjFile(projectName));
        files.put(projectName + "/Info.plist", generateInfoPlist(projectName));
        files.put(projectName + "/SceneDelegate.swift", generateSceneDelegate(projectName));
        files.put(projectName + "/AppDelegate.swift", generateAppDelegate(projectName));

        return files;
}

    private String generateInfoPlist(String projectName) {
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
            <key>UIApplicationSupportsIndirectInputEvents</key>
            <true/>
            <key>UILaunchStoryboardName</key>
            <string>LaunchScreen</string>
            <key>UIRequiredDeviceCapabilities</key>
            <array>
                <string>armv7</string>
            </array>
            <key>UISupportedInterfaceOrientations</key>
            <array>
                <string>UIInterfaceOrientationPortrait</string>
                <string>UIInterfaceOrientationLandscapeLeft</string>
                <string>UIInterfaceOrientationLandscapeRight</string>
            </array>
        </dict>
        </plist>
        """;
    }

    private String generateSceneDelegate(String projectName) {
        return """
        import UIKit

        class SceneDelegate: UIResponder, UIWindowSceneDelegate {
            var window: UIWindow?

            func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options: UIScene.ConnectionOptions) {
                guard let windowScene = (scene as? UIWindowScene) else { return }
                
                let window = UIWindow(windowScene: windowScene)
                let viewController = %sViewController()
                let navigationController = UINavigationController(rootViewController: viewController)
                
                window.rootViewController = navigationController
                window.makeKeyAndVisible()
                self.window = window
            }

            func sceneDidDisconnect(_ scene: UIScene) {
            }

            func sceneDidBecomeActive(_ scene: UIScene) {
            }

            func sceneWillResignActive(_ scene: UIScene) {
            }

            func sceneWillEnterForeground(_ scene: UIScene) {
            }

            func sceneDidEnterBackground(_ scene: UIScene) {
            }
        }
        """.formatted(projectName);
    }

    private String generateAppDelegate(String projectName) {
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

            // MARK: UISceneSession Lifecycle
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

            func application(
                _ application: UIApplication,
                didDiscardSceneSessions sceneSessions: Set<UISceneSession>
            ) {
            }
        }
        """;
    }

    private String generateXcodeProjFile(String projectName) {
        return """
        // !$*UTF8*$!
        {
            archiveVersion = 1;
            classes = {
            };
            objectVersion = 56;
            objects = {
                // Basic Xcode project structure
            };
            rootObject = ...;
        }
        """;
    }


    public Map<String, String> generateIOSMVVMStructure(String packageName, String projectName) {
        Map<String, String> files = new HashMap<>();
        // TODO: Implement iOS MVVM structure generation
        throw new UnsupportedOperationException("iOS MVVM structure generation not yet implemented");
        // return files;
    }




}