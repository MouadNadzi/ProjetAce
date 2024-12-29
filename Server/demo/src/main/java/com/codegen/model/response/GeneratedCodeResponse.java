package com.codegen.model.response;

import java.util.Map;
import java.util.List;

public class GeneratedCodeResponse {
    private String status;
    private String error;
    private Map<String, String> projectFiles;
    private List<String> projectStructure;
    private Map<String, String> dependencies;
    private String platform;
    private String pattern;

    // Getters and setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public Map<String, String> getProjectFiles() { return projectFiles; }
    public void setProjectFiles(Map<String, String> projectFiles) { this.projectFiles = projectFiles; }

    public List<String> getProjectStructure() { return projectStructure; }
    public void setProjectStructure(List<String> projectStructure) { this.projectStructure = projectStructure; }

    public Map<String, String> getDependencies() { return dependencies; }
    public void setDependencies(Map<String, String> dependencies) { this.dependencies = dependencies; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getPattern() { return pattern; }
    public void setPattern(String pattern) { this.pattern = pattern; }
}