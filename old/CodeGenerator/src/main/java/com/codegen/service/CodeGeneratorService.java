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

import com.codegen.model.entity.ParsedUMLModel;
import com.codegen.model.request.GenerationRequest;
import com.codegen.model.response.GeneratedCodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CodeGeneratorService {

    private final ProjectStructureService projectStructureService;
    private final ProjectStructureGenerator structureGenerator;
    @Autowired
    public CodeGeneratorService(ProjectStructureService projectStructureService,
                                ProjectStructureGenerator structureGenerator) {
        this.projectStructureService = projectStructureService;
        this.structureGenerator = structureGenerator;
    }

    // Add these methods for Android
    private String generateAndroidMVVMClass(String packageName, ParsedUMLModel.UMLClass umlClass) {
        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(";\n\n");
        code.append("import androidx.room.Entity;\n");
        code.append("import androidx.room.PrimaryKey;\n\n");

        code.append("@Entity(tableName = \"").append(umlClass.getName().toLowerCase()).append("s\")\n");
        code.append("public class ").append(umlClass.getName()).append(" {\n\n");

        // Generate fields
        code.append("    @PrimaryKey(autoGenerate = true)\n");
        code.append("    private int id;\n\n");

        for (ParsedUMLModel.UMLField field : umlClass.getFields()) {
            code.append("    private ").append(field.getType()).append(" ")
                    .append(field.getName()).append(";\n");
        }

        // Generate getters and setters
        code.append("\n    // Getters and Setters\n");
        code.append("    public int getId() { return id; }\n");
        code.append("    public void setId(int id) { this.id = id; }\n\n");

        for (ParsedUMLModel.UMLField field : umlClass.getFields()) {
            String capitalizedName = field.getName().substring(0, 1).toUpperCase() +
                    field.getName().substring(1);
            code.append("    public ").append(field.getType()).append(" get")
                    .append(capitalizedName).append("() { return ").append(field.getName())
                    .append("; }\n");
            code.append("    public void set").append(capitalizedName).append("(")
                    .append(field.getType()).append(" ").append(field.getName()).append(") { this.")
                    .append(field.getName()).append(" = ").append(field.getName()).append("; }\n\n");
        }

        code.append("}");
        return code.toString();
    }

    private String generateAndroidMVCClass(String packageName, ParsedUMLModel.UMLClass umlClass) {
        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(".model;\n\n");

        code.append("public class ").append(umlClass.getName()).append(" {\n\n");

        // Generate fields
        for (ParsedUMLModel.UMLField field : umlClass.getFields()) {
            code.append("    private ").append(field.getType()).append(" ")
                    .append(field.getName()).append(";\n");
        }

        // Generate constructor
        code.append("\n    public ").append(umlClass.getName()).append("() {}\n\n");

        // Generate getters and setters
        for (ParsedUMLModel.UMLField field : umlClass.getFields()) {
            String capitalizedName = field.getName().substring(0, 1).toUpperCase() +
                    field.getName().substring(1);
            code.append("    public ").append(field.getType()).append(" get")
                    .append(capitalizedName).append("() { return ").append(field.getName())
                    .append("; }\n");
            code.append("    public void set").append(capitalizedName).append("(")
                    .append(field.getType()).append(" ").append(field.getName()).append(") { this.")
                    .append(field.getName()).append(" = ").append(field.getName()).append("; }\n\n");
        }

        code.append("}");
        return code.toString();
    }

    private String generateAndroidMVPClass(String packageName, ParsedUMLModel.UMLClass umlClass) {
        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(".presenter;\n\n");

        // Generate interfaces
        code.append("public interface ").append(umlClass.getName()).append("Contract {\n\n");

        // View interface
        code.append("    interface View {\n");
        code.append("        void showLoading();\n");
        code.append("        void hideLoading();\n");
        code.append("        void showError(String message);\n");
        code.append("    }\n\n");

        // Presenter interface
        code.append("    interface Presenter {\n");
        code.append("        void start();\n");
        code.append("        void stop();\n");
        for (ParsedUMLModel.UMLField field : umlClass.getFields()) {
            code.append("        void update").append(field.getName().substring(0, 1).toUpperCase())
                    .append(field.getName().substring(1)).append("(")
                    .append(field.getType()).append(" value);\n");
        }
        code.append("    }\n");

        code.append("}");
        return code.toString();
    }

    // Add these methods for iOS
    private String generateIOSMVVMModel(ParsedUMLModel.UMLClass umlClass) {
        StringBuilder code = new StringBuilder();
        code.append("import Foundation\n\n");

        code.append("struct ").append(umlClass.getName()).append(": Codable {\n");

        // Generate properties
        for (ParsedUMLModel.UMLField field : umlClass.getFields()) {
            code.append("    let ").append(field.getName()).append(": ")
                    .append(convertToSwiftType(field.getType())).append("\n");
        }

        code.append("}\n");
        return code.toString();
    }

    private String generateIOSMVPModel(ParsedUMLModel.UMLClass umlClass) {
        StringBuilder code = new StringBuilder();
        code.append("import Foundation\n\n");

        // Generate model
        code.append("struct ").append(umlClass.getName()).append(" {\n");
        for (ParsedUMLModel.UMLField field : umlClass.getFields()) {
            code.append("    var ").append(field.getName()).append(": ")
                    .append(convertToSwiftType(field.getType())).append("\n");
        }
        code.append("}\n");

        return code.toString();
    }
    private String generateIOSMVCModel(ParsedUMLModel.UMLClass umlClass) {
        StringBuilder code = new StringBuilder();
        code.append("import Foundation\n\n");

        // Generate model class
        code.append("class ").append(umlClass.getName()).append(" {\n");

        // Generate properties
        for (ParsedUMLModel.UMLField field : umlClass.getFields()) {
            code.append("    var ").append(field.getName()).append(": ")
                    .append(convertToSwiftType(field.getType())).append("\n");
        }

        // Generate initializer
        code.append("\n    init(");
        List<ParsedUMLModel.UMLField> fields = umlClass.getFields();
        for (int i = 0; i < fields.size(); i++) {
            ParsedUMLModel.UMLField field = fields.get(i);
            code.append(field.getName()).append(": ")
                    .append(convertToSwiftType(field.getType()));
            if (i < fields.size() - 1) {
                code.append(", ");
            }
        }
        code.append(") {\n");

        // Set properties in initializer
        for (ParsedUMLModel.UMLField field : fields) {
            code.append("        self.").append(field.getName())
                    .append(" = ").append(field.getName()).append("\n");
        }
        code.append("    }\n");

        code.append("}\n");
        return code.toString();
    }

    private String convertToSwiftType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "string" -> "String";
            case "int" -> "Int";
            case "long" -> "Int64";
            case "double" -> "Double";
            case "float" -> "Float";
            case "boolean" -> "Bool";
            case "date" -> "Date";
            case "list" -> "Array<Any>";
            case "map" -> "Dictionary<String, Any>";
            default -> "Any";
        };
    }

    /*public GeneratedCodeResponse generateCode(GenerationRequest request) {
        try {
            String platform = request.getPlatform().toLowerCase().trim();
            String pattern = request.getPattern().toLowerCase().trim();
            String packageName = request.getPackageName();
            String projectName = request.getProjectName();

            // Generate project structure
            List<String> projectStructure = structureGenerator.generateProjectStructure(platform, pattern);

            // Generate project files
            Map<String, String> projectFiles = projectStructureService.generateProjectStructure(
                    packageName,
                    projectName,
                    platform,
                    pattern
            );

            // Get dependencies
            Map<String, String> dependencies = structureGenerator.getDependencies(platform, pattern);

            // Create response
            GeneratedCodeResponse response = new GeneratedCodeResponse();
            response.setProjectFiles(projectFiles);
            response.setProjectStructure(projectStructure);
            response.setDependencies(dependencies);
            response.setPlatform(platform);
            response.setPattern(pattern);
            response.setStatus("SUCCESS");

            return response;
        } catch (Exception e) {
            GeneratedCodeResponse errorResponse = new GeneratedCodeResponse();
            errorResponse.setStatus("ERROR");
            errorResponse.setError(e.getMessage());
            return errorResponse;
        }
    }*/
    public Map<String, String> generateFromUML(
            String platform,
            String pattern,
            String packageName,
            String projectName,
            ParsedUMLModel umlModel
    ) {
        // 1) Generate the base skeleton: Gradle files, Xcode project, etc.
        Map<String, String> files = projectStructureService.generateBaseStructure(
                platform, pattern, packageName, projectName
        );

        // 2) For each UML class, generate the actual code (model, entities, etc.)
        for (ParsedUMLModel.UMLClass umlClass : umlModel.getClasses()) {
            String classCode = "";
            String filePath  = "";

            // ---------------------------------------
            // ANDROID MVVM
            // ---------------------------------------
            if (platform.equalsIgnoreCase("android") && pattern.equalsIgnoreCase("mvvm")) {
                classCode = generateAndroidMVVMClass(packageName, umlClass);
                filePath  = "app/src/main/java/"
                        + packageName.replace('.', '/')
                        + "/model/"
                        + umlClass.getName() + ".java";
            }
            // ---------------------------------------
            // ANDROID MVC
            // ---------------------------------------
            else if (platform.equalsIgnoreCase("android") && pattern.equalsIgnoreCase("mvc")) {
                classCode = generateAndroidMVCClass(packageName, umlClass);
                filePath  = "app/src/main/java/"
                        + packageName.replace('.', '/')
                        + "/model/"
                        + umlClass.getName() + ".java";
            }
            // ---------------------------------------
            // ANDROID MVP
            // ---------------------------------------
            else if (platform.equalsIgnoreCase("android") && pattern.equalsIgnoreCase("mvp")) {
                classCode = generateAndroidMVPClass(packageName, umlClass);
                filePath  = "app/src/main/java/"
                        + packageName.replace('.', '/')
                        + "/presenter/"  // e.g. you might put it in a `presenter` package for MVP
                        + umlClass.getName() + "Presenter.java";
            }
            // ---------------------------------------
            // iOS MVC
            // ---------------------------------------
            else if (platform.equalsIgnoreCase("ios") && pattern.equalsIgnoreCase("mvc")) {
                classCode = generateIOSMVCModel(umlClass);
                filePath  = projectName + "/Models/" + umlClass.getName() + ".swift";
            }
            // ---------------------------------------
            // iOS MVVM
            // ---------------------------------------
            else if (platform.equalsIgnoreCase("ios") && pattern.equalsIgnoreCase("mvvm")) {
                classCode = generateIOSMVVMModel(umlClass);
                filePath  = projectName + "/ViewModels/" + umlClass.getName() + "ViewModel.swift";
            }
            // ---------------------------------------
            // iOS MVP
            // ---------------------------------------
            else if (platform.equalsIgnoreCase("ios") && pattern.equalsIgnoreCase("mvp")) {
                classCode = generateIOSMVPModel(umlClass);
                filePath  = projectName + "/Presenters/" + umlClass.getName() + "Presenter.swift";
            }
            // ---------------------------------------
            // UNSUPPORTED
            // ---------------------------------------
            else {
                throw new UnsupportedOperationException(
                        "Unsupported platform + pattern combination: "
                                + platform + " / " + pattern
                );
            }

            // 3) Put the generated code into the files map
            files.put(filePath, classCode);
        }

        // 4) Return the final map of filePath -> code content
        return files;
    }



    public String generateCode(GenerationRequest request) {
        String platform = request.getPlatform().toLowerCase().trim();
        String pattern = request.getPattern().toLowerCase().trim();

        return switch (platform) {
            case "android" -> switch (pattern) {
                case "mvvm" -> generateMVVMViewModel(request.getPackageName(), request.getProjectName());
                case "mvc" -> generateMVCController(request.getPackageName(), request.getProjectName());
                case "mvp" -> generateAndroidMVPMain(request.getPackageName(), request.getProjectName());
                default -> throw new UnsupportedOperationException("Unsupported pattern for Android: " + pattern);
            };
            case "ios" -> switch (pattern) {
                case "mvc" -> generateIOSMVCController(request.getPackageName(), request.getProjectName());
                case "mvvm" -> generateIOSMVVMViewModel(request.getPackageName(), request.getProjectName());
                case "mvp" -> generateIOSMVPMain(request.getPackageName(), request.getProjectName());
                default -> throw new UnsupportedOperationException("Unsupported pattern for iOS: " + pattern);
            };
            default -> throw new UnsupportedOperationException("Unsupported platform: " + platform);
        };
    }
    // Android MVP Implementation
    private String generateAndroidMVPMain(String packageName, String projectName) {
        return """
        package %s.presenter;
        
        import android.os.Bundle;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import %s.model.%sModel;
        import %s.presenter.%sPresenter;
        import %s.view.%sView;
        import java.util.List;
        
        public class %sActivity extends AppCompatActivity implements %sView {
            private %sPresenter presenter;
            private RecyclerView recyclerView;
            private %sAdapter adapter;
            
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                
                presenter = new %sPresenter(this, getApplication());
                setupViews();
                presenter.loadItems();
            }
            
            private void setupViews() {
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new %sAdapter(item -> presenter.onItemClicked(item));
                recyclerView.setAdapter(adapter);
                
                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setOnClickListener(v -> presenter.onAddItemClicked());
            }
            
            @Override
            public void showItems(List<%sModel> items) {
                adapter.setItems(items);
            }
            
            @Override
            public void showError(String message) {
                // Show error message using Snackbar or AlertDialog
            }
            
            @Override
            public void showAddItemDialog() {
                // Show dialog to add new item
            }
            
            @Override
            public void showEditItemDialog(%sModel item) {
                // Show dialog to edit item
            }
            
            @Override
            public void refreshItems() {
                presenter.loadItems();
            }
            
            @Override
            protected void onDestroy() {
                super.onDestroy();
                presenter.onDestroy();
            }
        }
        """.formatted(packageName, packageName, projectName, packageName, projectName,
                packageName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName);
    }

    private String generateAndroidMVPPresenter(String packageName, String projectName) {
        return """
        package %s.presenter;
        
        import android.app.Application;
        import %s.model.%sModel;
        import %s.model.repository.%sRepository;
        import %s.view.%sView;
        import java.util.List;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        
        public class %sPresenter {
            private final %sView view;
            private final %sRepository repository;
            private final ExecutorService executorService;
            
            public %sPresenter(%sView view, Application application) {
                this.view = view;
                this.repository = new %sRepository(application);
                this.executorService = Executors.newSingleThreadExecutor();
            }
            
            public void loadItems() {
                executorService.execute(() -> {
                    try {
                        List<%sModel> items = repository.getAllItems();
                        view.showItems(items);
                    } catch (Exception e) {
                        view.showError("Error loading items: " + e.getMessage());
                    }
                });
            }
            
            public void onAddItemClicked() {
                view.showAddItemDialog();
            }
            
            public void onItemClicked(%sModel item) {
                view.showEditItemDialog(item);
            }
            
            public void addItem(String title, String description) {
                if (title == null || title.trim().isEmpty()) {
                    view.showError("Title cannot be empty");
                    return;
                }
                
                %sModel item = new %sModel(title.trim(), description.trim());
                executorService.execute(() -> {
                    try {
                        repository.insert(item);
                        loadItems();
                    } catch (Exception e) {
                        view.showError("Error adding item: " + e.getMessage());
                    }
                });
            }
            
            public void updateItem(%sModel item) {
                if (item.getTitle() == null || item.getTitle().trim().isEmpty()) {
                    view.showError("Title cannot be empty");
                    return;
                }
                
                executorService.execute(() -> {
                    try {
                        repository.update(item);
                        loadItems();
                    } catch (Exception e) {
                        view.showError("Error updating item: " + e.getMessage());
                    }
                });
            }
            
            public void deleteItem(%sModel item) {
                executorService.execute(() -> {
                    try {
                        repository.delete(item);
                        loadItems();
                    } catch (Exception e) {
                        view.showError("Error deleting item: " + e.getMessage());
                    }
                });
            }
            
            public void onDestroy() {
                executorService.shutdown();
            }
        }
        """.formatted(packageName, packageName, projectName, packageName, projectName,
                packageName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName);
    }

    private String generateAndroidMVPView(String packageName, String projectName) {
        return """
        package %s.view;
        
        import %s.model.%sModel;
        import java.util.List;
        
        public interface %sView {
            void showItems(List<%sModel> items);
            void showError(String message);
            void showAddItemDialog();
            void showEditItemDialog(%sModel item);
            void refreshItems();
        }
        """.formatted(packageName, packageName, projectName, projectName, projectName, projectName);
    }

    private String generateAndroidMVPAdapter(String packageName, String projectName) {
        return """
        package %s.presenter;
        
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import android.widget.CheckBox;
        import androidx.recyclerview.widget.RecyclerView;
        import %s.model.%sModel;
        import java.util.ArrayList;
        import java.util.List;
        
        public class %sAdapter extends RecyclerView.Adapter<%sAdapter.ViewHolder> {
            private List<%sModel> items = new ArrayList<>();
            private final OnItemClickListener listener;
            
            public interface OnItemClickListener {
                void onItemClick(%sModel item);
            }
            
            public %sAdapter(OnItemClickListener listener) {
                this.listener = listener;
            }
            
            public void setItems(List<%sModel> items) {
                this.items = new ArrayList<>(items);
                notifyDataSetChanged();
            }
            
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_layout, parent, false);
                return new ViewHolder(view);
            }
            
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                holder.bind(items.get(position), listener);
            }
            
            @Override
            public int getItemCount() {
                return items.size();
            }
            
            static class ViewHolder extends RecyclerView.ViewHolder {
                private final TextView titleText;
                private final TextView descriptionText;
                private final CheckBox completedCheckBox;
                
                ViewHolder(View itemView) {
                    super(itemView);
                    titleText = itemView.findViewById(R.id.text_title);
                    descriptionText = itemView.findViewById(R.id.text_description);
                    completedCheckBox = itemView.findViewById(R.id.checkbox_completed);
                }
                
                void bind(final %sModel item, final OnItemClickListener listener) {
                    titleText.setText(item.getTitle());
                    descriptionText.setText(item.getDescription());
                    completedCheckBox.setChecked(item.isCompleted());
                    
                    itemView.setOnClickListener(v -> listener.onItemClick(item));
                }
            }
        }
        """.formatted(packageName, packageName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName);
    }

    private String generateAndroidMVPRepository(String packageName, String projectName) {
        return """
        package %s.model.repository;
        
        import android.app.Application;
        import %s.model.%sModel;
        import %s.model.database.%sDao;
        import %s.model.database.%sDatabase;
        import java.util.List;
        
        public class %sRepository {
            private final %sDao dao;
            
            public %sRepository(Application application) {
                %sDatabase database = %sDatabase.getDatabase(application);
                dao = database.itemDao();
            }
            
            public List<%sModel> getAllItems() {
                return dao.getAllItems();
            }
            
            public void insert(%sModel item) {
                dao.insert(item);
            }
            
            public void update(%sModel item) {
                dao.update(item);
            }
            
            public void delete(%sModel item) {
                dao.delete(item);
            }
        }
        """.formatted(packageName, packageName, projectName, packageName, projectName,
                packageName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName,
                projectName);
    }
    private String generateIOSMVPMain(String packageName, String projectName) {
        return """
        import UIKit
        
        class %sViewController: UIViewController, %sViewProtocol {
            // MARK: - Properties
            private let presenter: %sPresenterProtocol
            private let tableView = UITableView()
            private var items: [%sModel] = []
            
            // MARK: - Initialization
            init(presenter: %sPresenterProtocol) {
                self.presenter = presenter
                super.init(nibName: nil, bundle: nil)
            }
            
            required init?(coder: NSCoder) {
                fatalError("init(coder:) has not been implemented")
            }
            
            // MARK: - Lifecycle
            override func viewDidLoad() {
                super.viewDidLoad()
                setupUI()
                configureTableView()
                setupNavigationBar()
                presenter.viewDidLoad()
            }
            
            // MARK: - UI Setup
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
            
            // MARK: - Actions
            @objc private func addButtonTapped() {
                presenter.didTapAddButton()
            }
            
            // MARK: - ViewProtocol Implementation
            func showItems(_ items: [%sModel]) {
                self.items = items
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                }
            }
            
            func showError(_ error: Error) {
                DispatchQueue.main.async {
                    let alert = UIAlertController(
                        title: "Error",
                        message: error.localizedDescription,
                        preferredStyle: .alert
                    )
                    alert.addAction(UIAlertAction(title: "OK", style: .default))
                    self.present(alert, animated: true)
                }
            }
            
            func showAddItemDialog() {
                let alert = UIAlertController(
                    title: "Add Item",
                    message: nil,
                    preferredStyle: .alert
                )
                
                alert.addTextField { textField in
                    textField.placeholder = "Title"
                }
                
                alert.addTextField { textField in
                    textField.placeholder = "Description"
                }
                
                let addAction = UIAlertAction(title: "Add", style: .default) { [weak self] _ in
                    guard let title = alert.textFields?[0].text,
                          let description = alert.textFields?[1].text else { return }
                    self?.presenter.addItem(title: title, description: description)
                }
                
                alert.addAction(addAction)
                alert.addAction(UIAlertAction(title: "Cancel", style: .cancel))
                
                present(alert, animated: true)
            }
            
            func showEditItemDialog(for item: %sModel) {
                let alert = UIAlertController(
                    title: "Edit Item",
                    message: nil,
                    preferredStyle: .alert
                )
                
                alert.addTextField { textField in
                    textField.text = item.title
                    textField.placeholder = "Title"
                }
                
                alert.addTextField { textField in
                    textField.text = item.description
                    textField.placeholder = "Description"
                }
                
                let saveAction = UIAlertAction(title: "Save", style: .default) { [weak self] _ in
                    guard let title = alert.textFields?[0].text,
                          let description = alert.textFields?[1].text else { return }
                    var updatedItem = item
                    updatedItem.title = title
                    updatedItem.description = description
                    self?.presenter.updateItem(updatedItem)
                }
                
                alert.addAction(saveAction)
                alert.addAction(UIAlertAction(title: "Cancel", style: .cancel))
                
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
                presenter.didSelectItem(item)
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
                    if let item = item {
                        self?.presenter.deleteItem(item)
                    }
                    completion(true)
                }
                
                return UISwipeActionsConfiguration(actions: [deleteAction])
            }
        }
        """.formatted(projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName);
    }

    private String generateIOSMVPPresenter(String packageName, String projectName) {
        return """
        protocol %sPresenterProtocol {
            func viewDidLoad()
            func didTapAddButton()
            func didSelectItem(_ item: %sModel)
            func addItem(title: String, description: String)
            func updateItem(_ item: %sModel)
            func deleteItem(_ item: %sModel)
        }

        protocol %sViewProtocol: AnyObject {
            func showItems(_ items: [%sModel])
            func showError(_ error: Error)
            func showAddItemDialog()
            func showEditItemDialog(for item: %sModel)
        }

        class %sPresenter: %sPresenterProtocol {
            weak var view: %sViewProtocol?
            private let dataManager: %sDataManagerProtocol
            
            init(view: %sViewProtocol, dataManager: %sDataManagerProtocol = %sDataManager()) {
                self.view = view
                self.dataManager = dataManager
            }
            
            func viewDidLoad() {
                loadItems()
            }
            
            func didTapAddButton() {
                view?.showAddItemDialog()
            }
            
            func didSelectItem(_ item: %sModel) {
                view?.showEditItemDialog(for: item)
            }
            
            func addItem(title: String, description: String) {
                guard !title.isEmpty else {
                    view?.showError(%sError.invalidInput)
                    return
                }
                
                let item = %sModel(title: title, description: description)
                dataManager.addItem(item) { [weak self] result in
                    switch result {
                    case .success:
                        self?.loadItems()
                    case .failure(let error):
                        self?.view?.showError(error)
                    }
                }
            }
            
            func updateItem(_ item: %sModel) {
                guard !item.title.isEmpty else {
                    view?.showError(%sError.invalidInput)
                    return
                }
                
                dataManager.updateItem(item) { [weak self] result in
                    switch result {
                    case .success:
                        self?.loadItems()
                    case .failure(let error):
                        self?.view?.showError(error)
                    }
                }
            }
            
            func deleteItem(_ item: %sModel) {
                dataManager.deleteItem(item) { [weak self] result in
                    switch result {
                    case .success:
                        self?.loadItems()
                    case .failure(let error):
                        self?.view?.showError(error)
                    }
                }
            }
            
            private func loadItems() {
                dataManager.fetchItems { [weak self] result in
                    switch result {
                    case .success(let items):
                        self?.view?.showItems(items)
                    case .failure(let error):
                        self?.view?.showError(error)
                    }
                }
            }
        }
        """.formatted(projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName,
                projectName);
    }

    private String generateIOSMVPDataManager(String packageName, String projectName) {
        return """
        protocol %sDataManagerProtocol {
            func fetchItems(completion: @escaping (Result<[%sModel], Error>) -> Void)
            func addItem(_ item: %sModel, completion: @escaping (Result<Void, Error>) -> Void)
            func updateItem(_ item: %sModel, completion: @escaping (Result<Void, Error>) -> Void)
            func deleteItem(_ item: %sModel, completion: @escaping (Result<Void, Error>) -> Void)
        }

        enum %sError: LocalizedError {
            case invalidInput
            case failedToSaveData
            case failedToLoadData
            case itemNotFound
            
            var errorDescription: String? {
                switch self {
                case .invalidInput:
                    return "Invalid input data"
                case .failedToSaveData:
                    return "Failed to save data"
                case .failedToLoadData:
                    return "Failed to load data"
                case .itemNotFound:
                    return "Item not found"
                }
            }
        }

        class %sDataManager: %sDataManagerProtocol {
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
                projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName, projectName, projectName,
                projectName, projectName, projectName);
    }
    private String generateIOSMVPModel(String packageName, String projectName) {
        return """
        import Foundation
        
        struct %sModel: Codable, Identifiable {
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
            
            mutating func toggleCompletion() {
                isCompleted.toggle()
                updatedAt = Date()
            }
            
            mutating func update(title: String, description: String) {
                self.title = title
                self.description = description
                self.updatedAt = Date()
            }
        }

        extension %sModel: Equatable {
            static func == (lhs: %sModel, rhs: %sModel) -> Bool {
                lhs.id == rhs.id
            }
        }

        extension %sModel: Hashable {
            func hash(into hasher: inout Hasher) {
                hasher.combine(id)
            }
        }
        """.formatted(projectName, projectName, projectName, projectName, projectName);
    }

    private String generateIOSMVPTableViewCell(String packageName, String projectName) {
        return """
        import UIKit
        
        class %sTableViewCell: UITableViewCell {
            static let identifier = "%sTableViewCell"
            
            // MARK: - UI Components
            private let containerStackView: UIStackView = {
                let stack = UIStackView()
                stack.axis = .horizontal
                stack.spacing = 12
                stack.alignment = .center
                return stack
            }()
            
            private let contentStackView: UIStackView = {
                let stack = UIStackView()
                stack.axis = .vertical
                stack.spacing = 4
                return stack
            }()
            
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
                toggle.onTintColor = .systemBlue
                return toggle
            }()
            
            // MARK: - Properties
            private var onCompletionToggle: ((Bool) -> Void)?
            
            // MARK: - Initialization
            override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
                super.init(style: style, reuseIdentifier: reuseIdentifier)
                setupUI()
            }
            
            required init?(coder: NSCoder) {
                fatalError("init(coder:) has not been implemented")
            }
            
            // MARK: - UI Setup
            private func setupUI() {
                selectionStyle = .none
                
                contentView.addSubview(containerStackView)
                containerStackView.translatesAutoresizingMaskIntoConstraints = false
                
                containerStackView.addArrangedSubview(contentStackView)
                containerStackView.addArrangedSubview(completedSwitch)
                
                contentStackView.addArrangedSubview(titleLabel)
                contentStackView.addArrangedSubview(descriptionLabel)
                
                NSLayoutConstraint.activate([
                    containerStackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 12),
                    containerStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
                    containerStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
                    containerStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -12)
                ])
                
                completedSwitch.addTarget(self, action: #selector(switchToggled), for: .valueChanged)
            }
            
            // MARK: - Configuration
            func configure(with item: %sModel, onCompletionToggle: @escaping (Bool) -> Void) {
                titleLabel.text = item.title
                descriptionLabel.text = item.description
                completedSwitch.isOn = item.isCompleted
                self.onCompletionToggle = onCompletionToggle
                
                updateTitleStyle(isCompleted: item.isCompleted)
            }
            
            private func updateTitleStyle(isCompleted: Bool) {
                if isCompleted {
                    titleLabel.attributedText = NSAttributedString(
                        string: titleLabel.text ?? "",
                        attributes: [.strikethroughStyle: NSUnderlineStyle.single.rawValue]
                    )
                } else {
                    titleLabel.attributedText = nil
                    titleLabel.text = titleLabel.text
                }
            }
            
            // MARK: - Actions
            @objc private func switchToggled() {
                onCompletionToggle?(completedSwitch.isOn)
                updateTitleStyle(isCompleted: completedSwitch.isOn)
            }
            
            // MARK: - Reuse
            override func prepareForReuse() {
                super.prepareForReuse()
                titleLabel.attributedText = nil
                titleLabel.text = nil
                descriptionLabel.text = nil
                completedSwitch.isOn = false
                onCompletionToggle = nil
            }
        }
        """.formatted(projectName, projectName, projectName);
    }

    private String generateIOSMVPFactory(String packageName, String projectName) {
        return """
        class %sFactory {
            static func createMainScreen() -> UIViewController {
                let dataManager = %sDataManager()
                let viewController = %sViewController()
                let presenter = %sPresenter(view: viewController, dataManager: dataManager)
                viewController.presenter = presenter
                return UINavigationController(rootViewController: viewController)
            }
        }
        """.formatted(projectName, projectName, projectName, projectName);
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

    /*public String generateIOSMVCModel(String projectName) {
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
    }*/

    // In CodeGeneratorService class
    public String generateIOSMVCModel(String projectName, String packageName) {
        return """
        import Foundation
        
        // %s
        // Project: %s
        // Package: %s
        
        class %sModel {
            // Properties
            private var id: Int
            private var title: String
            private var description: String
            private var createdAt: Date
            
            // MARK: - Initialization
            init(id: Int = 0, title: String = "", description: String = "") {
                self.id = id
                self.title = title
                self.description = description
                self.createdAt = Date()
            }
            
            // MARK: - Getters and Setters
            func getId() -> Int {
                return id
            }
            
            func setId(_ newId: Int) {
                self.id = newId
            }
            
            func getTitle() -> String {
                return title
            }
            
            func setTitle(_ newTitle: String) {
                self.title = newTitle
            }
            
            func getDescription() -> String {
                return description
            }
            
            func setDescription(_ newDescription: String) {
                self.description = newDescription
            }
            
            func getCreatedAt() -> Date {
                return createdAt
            }
        }
        
        // MARK: - Codable Extension
        extension %sModel: Codable {
            enum CodingKeys: String, CodingKey {
                case id
                case title
                case description
                case createdAt
            }
        }
        """.formatted(
                "Generated Model for iOS MVC Pattern",
                projectName,
                packageName,
                projectName,
                projectName
        );
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