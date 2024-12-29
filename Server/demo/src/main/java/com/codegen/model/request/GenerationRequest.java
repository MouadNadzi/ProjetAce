package com.codegen.model.request;

public class GenerationRequest {
    private String platform;
    private String pattern;
    private String packageName;
    private String projectName;
    private String umlContent;

    // Getters and setters
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getPattern() { return pattern; }
    public void setPattern(String pattern) { this.pattern = pattern; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getUmlContent() { return umlContent; }
    public void setUmlContent(String umlContent) { this.umlContent = umlContent; }
}