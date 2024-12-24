package com.codegen.model.request;

public class GenerationRequest {
    private String platform;
    private String pattern;
    private String projectName;
    private String packageName;

    // Default constructor
    public GenerationRequest() {}

    // Constructor with all fields
    public GenerationRequest(String platform, String pattern, String projectName, String packageName) {
        this.platform = platform;
        this.pattern = pattern;
        this.projectName = projectName;
        this.packageName = packageName;
    }

    // Getters
    public String getPlatform() {
        return platform;
    }

    public String getPattern() {
        return pattern;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getPackageName() {
        return packageName;
    }

    // Setters
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}