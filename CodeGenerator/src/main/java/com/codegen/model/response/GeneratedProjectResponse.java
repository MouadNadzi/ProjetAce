package com.codegen.model.response;

import java.util.List;
import java.util.Map;

public class GeneratedProjectResponse {
    private String mainCode;
    private String fileName;
    private String language;
    private Map<String, String> projectFiles; // path -> content
    private List<String> projectStructure;  // Directory structure
    private Map<String, String> dependencies; // Dependencies to add

    // Constructor
    public GeneratedProjectResponse(String mainCode, String fileName, String language,
                                    Map<String, String> projectFiles,
                                    List<String> projectStructure,
                                    Map<String, String> dependencies) {
        this.mainCode = mainCode;
        this.fileName = fileName;
        this.language = language;
        this.projectFiles = projectFiles;
        this.projectStructure = projectStructure;
        this.dependencies = dependencies;
    }

    // Getters and setters
    public String getMainCode() { return mainCode; }
    public void setMainCode(String mainCode) { this.mainCode = mainCode; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Map<String, String> getProjectFiles() { return projectFiles; }
    public void setProjectFiles(Map<String, String> projectFiles) { this.projectFiles = projectFiles; }

    public List<String> getProjectStructure() { return projectStructure; }
    public void setProjectStructure(List<String> projectStructure) { this.projectStructure = projectStructure; }

    public Map<String, String> getDependencies() { return dependencies; }
    public void setDependencies(Map<String, String> dependencies) { this.dependencies = dependencies; }
}