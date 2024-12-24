/*package com.codegen.service;

import com.codegen.exception.TemplateNotFoundException;
import com.codegen.model.entity.Pattern;
import com.codegen.model.request.GenerationRequest;
import com.codegen.model.response.GeneratedCodeResponse;
import com.codegen.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CodeGeneratorService {

    private final TemplateRepository templateRepository;

    @Autowired
    public CodeGeneratorService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public GeneratedCodeResponse generateCode(GenerationRequest request) {
        Pattern pattern = templateRepository.findByPlatformAndName(
                request.getPlatform(),
                request.getPattern()
        ).orElseThrow(() -> new TemplateNotFoundException("Template not found"));

        Map<String, String> variables = new HashMap<>();
        variables.put("projectName", request.getProjectName());
        variables.put("packageName", request.getPackageName());

        String processedCode = processTemplate(pattern.getTemplateContent(), variables);

        return new GeneratedCodeResponse(
                processedCode,
                generateFileName(request),
                determineLanguage(request.getPlatform())
        );
    }

    private String processTemplate(String template, Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    private String generateFileName(GenerationRequest request) {
        String prefix = request.getProjectName();
        String suffix = determineSuffix(request.getPlatform(), request.getPattern());
        return prefix + suffix;
    }

    private String determineSuffix(String platform, String pattern) {
        if ("android".equalsIgnoreCase(platform)) {
            return "Activity.java";
        } else if ("ios".equalsIgnoreCase(platform)) {
            return "ViewController.swift";
        }
        return ".txt";
    }

    private String determineLanguage(String platform) {
        return "android".equalsIgnoreCase(platform) ? "java" : "swift";
    }
}*/

package com.codegen.service;

import com.codegen.model.request.GenerationRequest;
import org.springframework.stereotype.Service;

@Service
public class CodeGeneratorService {

    /*public String generateCode(GenerationRequest request) {
        if ("android".equals(request.getPlatform())) {
            if ("mvvm".equals(request.getPattern())) {
                return generateMVVMViewModel(request.getPackageName(), request.getProjectName());
            } else if ("mvc".equals(request.getPattern())) {
                return generateMVCController(request.getPackageName(), request.getProjectName());
            }
        }
        throw new UnsupportedOperationException("Unsupported platform or pattern");
    }*/
    public String generateCode(GenerationRequest request) {
        String platform = request.getPlatform().toLowerCase().trim();
        String pattern = request.getPattern().toLowerCase().trim();

        return switch (platform) {
            case "android" -> switch (pattern) {
                case "mvvm" -> generateMVVMViewModel(request.getPackageName(), request.getProjectName());
                case "mvc" -> generateMVCController(request.getPackageName(), request.getProjectName());
                default -> throw new UnsupportedOperationException("Unsupported pattern for Android: " + pattern);
            };
            case "ios" -> switch (pattern) {
                case "mvc" -> generateIOSMVCController(request.getPackageName(), request.getProjectName());

                case "mvvm" -> generateIOSMVVMViewModel(request.getPackageName(), request.getProjectName());
                default -> throw new UnsupportedOperationException("Unsupported pattern for iOS: " + pattern);
            };
            default -> throw new UnsupportedOperationException("Unsupported platform: " + platform);
        };
    }

    public String generateIOSMVCController(String packageName, String projectName) {
        return """
        import UIKit
        
        class %sViewController: UIViewController {
            // MARK: - Properties
            private let tableView = UITableView()
            private let dataManager = %sDataManager()
            private var items: [%sModel] = []
            
            // MARK: - Lifecycle Methods
            override func viewDidLoad() {
                super.viewDidLoad()
                setupUI()
                configureTableView()
                setupNavigationBar()
                loadData()
            }
            
            // MARK: - Setup
            private func setupUI() {
                view.backgroundColor = .systemBackground
                setupTableViewConstraints()
            }
            
            private func setupTableViewConstraints() {
                view.addSubview(tableView)
                tableView.translatesAutoresizingMaskIntoConstraints = false
                NSLayoutConstraint.activate([
                    tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
                    tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
                    tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
                    tableView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
                ])
            }
            
            private func configureTableView() {
                tableView.delegate = self
                tableView.dataSource = self
                tableView.register(%sTableViewCell.self, forCellReuseIdentifier: %sTableViewCell.identifier)
                tableView.rowHeight = UITableView.automaticDimension
                tableView.estimatedRowHeight = 80
            }
            
            private func setupNavigationBar() {
                title = "%s"
                navigationItem.rightBarButtonItem = UIBarButtonItem(
                    barButtonSystemItem: .add,
                    target: self,
                    action: #selector(addButtonTapped)
                )
            }
            
            private func loadData() {
                dataManager.fetchItems { [weak self] result in
                    switch result {
                    case .success(let items):
                        self?.items = items
                        DispatchQueue.main.async {
                            self?.tableView.reloadData()
                        }
                    case .failure(let error):
                        self?.showError(error)
                    }
                }
            }
            
            // MARK: - Actions
            @objc private func addButtonTapped() {
                showAddItemAlert()
            }
            
            // MARK: - Private Methods
            private func showError(_ error: Error) {
                let alert = UIAlertController(
                    title: "Error",
                    message: error.localizedDescription,
                    preferredStyle: .alert
                )
                alert.addAction(UIAlertAction(title: "OK", style: .default))
                present(alert, animated: true)
            }
        }

        // MARK: - UITableViewDataSource
        extension %sViewController: UITableViewDataSource {
            func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
                return items.count
            }
            
            func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
                guard let cell = tableView.dequeueReusableCell(
                    withIdentifier: %sTableViewCell.identifier,
                    for: indexPath
                ) as? %sTableViewCell else {
                    return UITableViewCell()
                }
                
                let item = items[indexPath.row]
                cell.configure(with: item)
                return cell
            }
        }

        // MARK: - UITableViewDelegate
        extension %sViewController: UITableViewDelegate {
            func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
                tableView.deselectRow(at: indexPath, animated: true)
                let item = items[indexPath.row]
                showEditAlert(for: item)
            }
        }
        """.formatted(projectName, projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName);
    }

    public String generateIOSMVVMViewModel(String packageName, String projectName) {
        return """
        import Foundation
        import Combine
        
        class %sViewModel {
            @Published private(set) var items: [%sModel] = []
            private let dataManager: %sDataManager
            
            init(dataManager: %sDataManager = %sDataManager()) {
                self.dataManager = dataManager
                loadItems()
            }
            
            func loadItems() {
                dataManager.fetchItems { [weak self] result in
                    switch result {
                    case .success(let items):
                        self?.items = items
                    case .failure(let error):
                        // Handle error
                        break
                    }
                }
            }
            
            func addItem(_ item: %sModel) {
                dataManager.addItem(item) { [weak self] result in
                    if case .success = result {
                        self?.loadItems()
                    }
                }
            }
            
            func updateItem(_ item: %sModel) {
                dataManager.updateItem(item) { [weak self] result in
                    if case .success = result {
                        self?.loadItems()
                    }
                }
            }
            
            func deleteItem(_ item: %sModel) {
                dataManager.deleteItem(item) { [weak self] result in
                    if case .success = result {
                        self?.loadItems()
                    }
                }
            }
        }
        """.formatted(projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName);
    }




    public String generateMVVMActivity(String packageName, String projectName) {
        return """
        package %s;
        
        import android.os.Bundle;
        import android.view.View;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.lifecycle.ViewModelProvider;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        
        public class %sActivity extends AppCompatActivity {
            private %sViewModel viewModel;
            private RecyclerView recyclerView;
            private ItemAdapter adapter;
            
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                
                setupViewModel();
                setupViews();
                observeViewModel();
            }
            
            private void setupViewModel() {
                viewModel = new ViewModelProvider(this).get(%sViewModel.class);
            }
            
            private void setupViews() {
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new ItemAdapter();
                recyclerView.setAdapter(adapter);
                
                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setOnClickListener(v -> showAddDialog());
            }
            
            private void observeViewModel() {
                viewModel.getItems().observe(this, items -> {
                    adapter.submitList(items);
                });
            }
            
            private void showAddDialog() {
                // Implementation for showing add dialog
            }
        }
        """.formatted(packageName, projectName, projectName, projectName);
    }

    public String generateMVVMViewModel(String packageName, String projectName) {
        return """
        package %s;
        
        import android.app.Application;
        import androidx.lifecycle.AndroidViewModel;
        import androidx.lifecycle.LiveData;
        import java.util.List;
        
        public class %sViewModel extends AndroidViewModel {
            private final %sRepository repository;
            private final LiveData<List<%sModel>> items;
            
            public %sViewModel(Application application) {
                super(application);
                repository = new %sRepository(application);
                items = repository.getAllItems();
            }
            
            public LiveData<List<%sModel>> getItems() {
                return items;
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
        """.formatted(packageName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName, projectName);
    }

    public String generateMVVMModel(String packageName, String projectName) {
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
            private long timestamp;
            
            public %sModel(String title, String description) {
                this.title = title;
                this.description = description;
                this.completed = false;
                this.timestamp = System.currentTimeMillis();
            }
            
            // Getters and Setters
            public int getId() { return id; }
            public void setId(int id) { this.id = id; }
            
            public String getTitle() { return title; }
            public void setTitle(String title) { this.title = title; }
            
            public String getDescription() { return description; }
            public void setDescription(String description) { this.description = description; }
            
            public boolean isCompleted() { return completed; }
            public void setCompleted(boolean completed) { this.completed = completed; }
            
            public long getTimestamp() { return timestamp; }
            public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        }
        """.formatted(packageName, projectName, projectName);
    }

    public String generateMVVMRepository(String packageName, String projectName) {
        return """
        package %s;
        
        import android.app.Application;
        import androidx.lifecycle.LiveData;
        import java.util.List;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        
        public class %sRepository {
            private final %sDao itemDao;
            private final LiveData<List<%sModel>> allItems;
            private final ExecutorService executorService;
            
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
        """.formatted(packageName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName,
                projectName, projectName, projectName);
    }



    public String generateMVVMLayout(String projectName) {
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


    // MVC Components
    public String generateMVCController(String packageName, String projectName) {
        return """
        package %s.controller;
        
        import android.app.Application;
        import %s.model.%sModel;
        import %s.model.repository.%sRepository;
        import %s.controller.callbacks.%sCallbacks;
        import java.util.List;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        
        public class %sController {
            private final %sRepository repository;
            private final ExecutorService executorService;
            private final %sCallbacks callbacks;
            
            public %sController(Application application, %sCallbacks callbacks) {
                this.repository = new %sRepository(application);
                this.executorService = Executors.newSingleThreadExecutor();
                this.callbacks = callbacks;
            }
            
            public void loadItems() {
                executorService.execute(() -> {
                    try {
                        List<%sModel> items = repository.getAllItems();
                        callbacks.onItemsLoaded(items);
                    } catch (Exception e) {
                        callbacks.onError("Error loading items: " + e.getMessage());
                    }
                });
            }
            
            public void addItem(String title, String description) {
                if (title == null || title.trim().isEmpty()) {
                    callbacks.onError("Title cannot be empty");
                    return;
                }
                
                %sModel item = new %sModel(title.trim(), description.trim());
                executorService.execute(() -> {
                    try {
                        repository.insert(item);
                        loadItems();
                    } catch (Exception e) {
                        callbacks.onError("Error adding item: " + e.getMessage());
                    }
                });
            }
            
            public void updateItem(%sModel item) {
                if (item.getTitle() == null || item.getTitle().trim().isEmpty()) {
                    callbacks.onError("Title cannot be empty");
                    return;
                }
                
                executorService.execute(() -> {
                    try {
                        repository.update(item);
                        loadItems();
                    } catch (Exception e) {
                        callbacks.onError("Error updating item: " + e.getMessage());
                    }
                });
            }
            
            public void deleteItem(%sModel item) {
                executorService.execute(() -> {
                    try {
                        repository.delete(item);
                        loadItems();
                    } catch (Exception e) {
                        callbacks.onError("Error deleting item: " + e.getMessage());
                    }
                });
            }
            
            public void cleanup() {
                executorService.shutdown();
            }
        }
        """.formatted(packageName, packageName, projectName, packageName, projectName, packageName, projectName,
                projectName, projectName, projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName);
    }

    public String generateMVCLayout(String projectName) {
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

                <androidx.appcompat.widget.Toolbar
                    android:id="@+id/toolbar"
                    android:layout_width="match_parent"
                    android:layout_height="?attr/actionBarSize"
                    android:background="?attr/colorPrimary"
                    app:title="@string/app_name"/>

            </com.google.android.material.appbar.AppBarLayout>

            <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/recyclerView"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                app:layout_behavior="@string/appbar_scrolling_view_behavior"/>

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

    public String generateMVCItemLayout() {
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
                    android:id="@+id/text_title"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:textSize="18sp"
                    android:textStyle="bold"/>

                <TextView
                    android:id="@+id/text_description"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="8dp"/>

                <CheckBox
                    android:id="@+id/checkbox_completed"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="8dp"
                    android:text="@string/mark_completed"/>

            </LinearLayout>

        </com.google.android.material.card.MaterialCardView>
        """;
    }

    public String generateMVCCallbacks(String packageName, String projectName) {
        return """
        package %s.controller.callbacks;

        import %s.model.%sModel;
        import java.util.List;

        public interface %sCallbacks {
            void onItemsLoaded(List<%sModel> items);
            void onError(String message);
            void onItemAdded(%sModel item);
            void onItemUpdated(%sModel item);
            void onItemDeleted(%sModel item);
        }
        """.formatted(packageName, packageName, projectName, projectName,
                projectName, projectName, projectName, projectName);
    }

    public String generateMVCDao(String packageName, String projectName) {
        return """
        package %s.model.database;

        import androidx.room.*;
        import %s.model.%sModel;
        import java.util.List;

        @Dao
        public interface %sDao {
            @Query("SELECT * FROM items ORDER BY id DESC")
            List<%sModel> getAllItems();

            @Query("SELECT * FROM items WHERE id = :id")
            %sModel getItemById(int id);

            @Insert(onConflict = OnConflictStrategy.REPLACE)
            void insert(%sModel item);

            @Update
            void update(%sModel item);

            @Delete
            void delete(%sModel item);

            @Query("SELECT * FROM items WHERE title LIKE :search OR description LIKE :search")
            List<%sModel> searchItems(String search);
        }
        """.formatted(packageName, packageName, projectName, projectName,
                projectName, projectName, projectName, projectName,
                projectName, projectName);
    }

    public String generateDateConverter(String packageName) {
        return """
        package %s.model.database;

        import androidx.room.TypeConverter;
        import java.util.Date;

        public class DateConverter {
            @TypeConverter
            public static Date fromTimestamp(Long value) {
                return value == null ? null : new Date(value);
            }

            @TypeConverter
            public static Long dateToTimestamp(Date date) {
                return date == null ? null : date.getTime();
            }
        }
        """.formatted(packageName);
    }


    public String generateIOSMVCViewController(String projectName) {
        return """
        import UIKit
        
        class %sViewController: UIViewController {
            
            private let tableView = UITableView()
            private let dataManager = %sDataManager()
            private var items: [%sModel] = []
            
            override func viewDidLoad() {
                super.viewDidLoad()
                setupUI()
                configureTableView()
                setupNavigationBar()
                loadData()
            }
            
            private func setupUI() {
                view.backgroundColor = .systemBackground
                
                // Setup TableView constraints
                view.addSubview(tableView)
                tableView.translatesAutoresizingMaskIntoConstraints = false
                NSLayoutConstraint.activate([
                    tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
                    tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
                    tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
                    tableView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
                ])
            }
            
            private func configureTableView() {
                tableView.delegate = self
                tableView.dataSource = self
                tableView.register(%sTableViewCell.self, forCellReuseIdentifier: %sTableViewCell.identifier)
                tableView.rowHeight = UITableView.automaticDimension
                tableView.estimatedRowHeight = 80
            }
            
            private func setupNavigationBar() {
                title = "%s"
                navigationItem.rightBarButtonItem = UIBarButtonItem(
                    barButtonSystemItem: .add,
                    target: self,
                    action: #selector(addButtonTapped)
                )
            }
            
            private func loadData() {
                dataManager.fetchItems { [weak self] result in
                    switch result {
                    case .success(let items):
                        self?.items = items
                        DispatchQueue.main.async {
                            self?.tableView.reloadData()
                        }
                    case .failure(let error):
                        self?.showError(error)
                    }
                }
            }
            
            @objc private func addButtonTapped() {
                let alertController = UIAlertController(
                    title: "Add Item",
                    message: nil,
                    preferredStyle: .alert
                )
                
                alertController.addTextField { textField in
                    textField.placeholder = "Title"
                }
                
                alertController.addTextField { textField in
                    textField.placeholder = "Description"
                }
                
                let addAction = UIAlertAction(title: "Add", style: .default) { [weak self] _ in
                    guard let title = alertController.textFields?[0].text,
                          let description = alertController.textFields?[1].text else { return }
                    
                    let newItem = %sModel(title: title, description: description)
                    self?.dataManager.addItem(newItem) { result in
                        switch result {
                        case .success:
                            self?.loadData()
                        case .failure(let error):
                            self?.showError(error)
                        }
                    }
                }
                
                alertController.addAction(addAction)
                alertController.addAction(UIAlertAction(title: "Cancel", style: .cancel))
                
                present(alertController, animated: true)
            }
            
            private func showError(_ error: Error) {
                DispatchQueue.main.async { [weak self] in
                    let alert = UIAlertController(
                        title: "Error",
                        message: error.localizedDescription,
                        preferredStyle: .alert
                    )
                    alert.addAction(UIAlertAction(title: "OK", style: .default))
                    self?.present(alert, animated: true)
                }
            }
        }

        // MARK: - UITableViewDataSource
        extension %sViewController: UITableViewDataSource {
            func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
                return items.count
            }
            
            func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
                guard let cell = tableView.dequeueReusableCell(
                    withIdentifier: %sTableViewCell.identifier,
                    for: indexPath
                ) as? %sTableViewCell else {
                    return UITableViewCell()
                }
                
                let item = items[indexPath.row]
                cell.configure(with: item)
                return cell
            }
        }

        // MARK: - UITableViewDelegate
        extension %sViewController: UITableViewDelegate {
            func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
                tableView.deselectRow(at: indexPath, animated: true)
                let item = items[indexPath.row]
                showEditAlert(for: item)
            }
            
            func tableView(
                _ tableView: UITableView,
                trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath
            ) -> UISwipeActionsConfiguration? {
                let deleteAction = UIContextualAction(
                    style: .destructive,
                    title: "Delete"
                ) { [weak self] _, _, completion in
                    let item = self?.items[indexPath.row]
                    self?.deleteItem(item)
                    completion(true)
                }
                
                return UISwipeActionsConfiguration(actions: [deleteAction])
            }
        }
        """.formatted(projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName, projectName);
    }

    public String generateIOSMVCModel(String projectName) {
        return """
        import Foundation
        
        struct %sModel: Codable {
            let id: UUID
            var title: String
            var description: String
            var isCompleted: Bool
            let createdAt: Date
            var updatedAt: Date
            
            init(title: String, description: String) {
                self.id = UUID()
                self.title = title
                self.description = description
                self.isCompleted = false
                self.createdAt = Date()
                self.updatedAt = Date()
            }
        }

        enum %sError: LocalizedError {
            case failedToSaveData
            case failedToLoadData
            case itemNotFound
            
            var errorDescription: String? {
                switch self {
                case .failedToSaveData:
                    return "Failed to save data"
                case .failedToLoadData:
                    return "Failed to load data"
                case .itemNotFound:
                    return "Item not found"
                }
            }
        }
        """.formatted(projectName, projectName);
    }

    public String generateIOSMVCDataManager(String projectName) {
        return """
        import Foundation
        
        class %sDataManager {
            private let userDefaults = UserDefaults.standard
            private let itemsKey = "%sItems"
            
            func fetchItems(completion: @escaping (Result<[%sModel], Error>) -> Void) {
                guard let data = userDefaults.data(forKey: itemsKey) else {
                    completion(.success([]))
                    return
                }
                
                do {
                    let items = try JSONDecoder().decode([%sModel].self, from: data)
                    completion(.success(items))
                } catch {
                    completion(.failure(%sError.failedToLoadData))
                }
            }
            
            func addItem(_ item: %sModel, completion: @escaping (Result<Void, Error>) -> Void) {
                fetchItems { [weak self] result in
                    switch result {
                    case .success(var items):
                        items.append(item)
                        self?.saveItems(items, completion: completion)
                    case .failure(let error):
                        completion(.failure(error))
                    }
                }
            }
            
            func updateItem(_ item: %sModel, completion: @escaping (Result<Void, Error>) -> Void) {
                fetchItems { [weak self] result in
                    switch result {
                    case .success(var items):
                        guard let index = items.firstIndex(where: { $0.id == item.id }) else {
                            completion(.failure(%sError.itemNotFound))
                            return
                        }
                        items[index] = item
                        self?.saveItems(items, completion: completion)
                    case .failure(let error):
                        completion(.failure(error))
                    }
                }
            }
            
            func deleteItem(_ item: %sModel, completion: @escaping (Result<Void, Error>) -> Void) {
                fetchItems { [weak self] result in
                    switch result {
                    case .success(var items):
                        items.removeAll { $0.id == item.id }
                        self?.saveItems(items, completion: completion)
                    case .failure(let error):
                        completion(.failure(error))
                    }
                }
            }
            
            private func saveItems(_ items: [%sModel], completion: @escaping (Result<Void, Error>) -> Void) {
                do {
                    let data = try JSONEncoder().encode(items)
                    userDefaults.set(data, forKey: itemsKey)
                    completion(.success(()))
                } catch {
                    completion(.failure(%sError.failedToSaveData))
                }
            }
        }
        """.formatted(projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName, projectName);
    }

    public String generateIOSMVCTableViewCell(String projectName) {
        return """
        import UIKit
        
        class %sTableViewCell: UITableViewCell {
            static let identifier = "%sTableViewCell"
            
            private let titleLabel: UILabel = {
                let label = UILabel()
                label.font = .systemFont(ofSize: 17, weight: .semibold)
                label.numberOfLines = 0
                return label
            }()
            
            private let descriptionLabel: UILabel = {
                let label = UILabel()
                label.font = .systemFont(ofSize: 14)
                label.textColor = .secondaryLabel
                label.numberOfLines = 0
                return label
            }()
            
            private let completedSwitch: UISwitch = {
                let toggle = UISwitch()
                return toggle
            }()
            
            override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
                super.init(style: style, reuseIdentifier: reuseIdentifier)
                setupUI()
            }
            
            required init?(coder: NSCoder) {
                fatalError("init(coder:) has not been implemented")
            }
            
            private func setupUI() {
                let stackView = UIStackView(arrangedSubviews: [titleLabel, descriptionLabel])
                stackView.axis = .vertical
                stackView.spacing = 4
                
                contentView.addSubview(stackView)
                contentView.addSubview(completedSwitch)
                
                stackView.translatesAutoresizingMaskIntoConstraints = false
                completedSwitch.translatesAutoresizingMaskIntoConstraints = false
                
                NSLayoutConstraint.activate([
                    stackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 12),
                    stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
                    stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -12),
                    stackView.trailingAnchor.constraint(equalTo: completedSwitch.leadingAnchor, constant: -12),
                    
                    completedSwitch.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
                    completedSwitch.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16)
                ])
            }
            
            func configure(with item: %sModel) {
                titleLabel.text = item.title
                descriptionLabel.text = item.description
                completedSwitch.isOn = item.isCompleted
                
                if item.isCompleted {
                    titleLabel.attributedText = NSAttributedString(
                        string: item.title,
                        attributes: [.strikethroughStyle: NSUnderlineStyle.single.rawValue]
                    )
                } else {
                    titleLabel.attributedText = nil
                    titleLabel.text = item.title
                }
            }
        }
        """.formatted(projectName, projectName, projectName);
    }

}