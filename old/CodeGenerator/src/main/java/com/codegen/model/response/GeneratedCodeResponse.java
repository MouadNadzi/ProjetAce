/*package com.codegen.model.response;

public class GeneratedCodeResponse {
    private String code;
    private String fileName;
    private String language;

    public GeneratedCodeResponse(String code, String fileName, String language) {
        this.code = code;
        this.fileName = fileName;
        this.language = language;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLanguage() {
        return language;
    }

    // Setters
    public void setCode(String code) {
        this.code = code;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}*/
/*
package com.codegen.model.response;

import java.util.List;
import java.util.Map;

public class GeneratedCodeResponse {
    private String code;
    private String fileName;
    private String language;
    private Map<String, String> projectFiles;
    private List<String> projectStructure;
    private Map<String, String> dependencies;

    public GeneratedCodeResponse(
            String code,
            String fileName,
            String language,
            Map<String, String> projectFiles,
            List<String> projectStructure,
            Map<String, String> dependencies) {
        this.code = code;
        this.fileName = fileName;
        this.language = language;
        this.projectFiles = projectFiles;
        this.projectStructure = projectStructure;
        this.dependencies = dependencies;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<String, String> getProjectFiles() {
        return projectFiles;
    }

    public void setProjectFiles(Map<String, String> projectFiles) {
        this.projectFiles = projectFiles;
    }

    public List<String> getProjectStructure() {
        return projectStructure;
    }

    public void setProjectStructure(List<String> projectStructure) {
        this.projectStructure = projectStructure;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<String, String> dependencies) {
        this.dependencies = dependencies;
    }
}*/

/*
package com.codegen.model.response;

import java.util.List;
import java.util.Map;

public class GeneratedCodeResponse {
    private String code;
    private String fileName;
    private String language;
    private Map<String, String> projectFiles;
    private List<String> projectStructure;
    private Map<String, String> dependencies;

    public GeneratedCodeResponse(
            String code,
            String fileName,
            String language,
            Map<String, String> projectFiles,
            List<String> projectStructure,
            Map<String, String> dependencies) {
        this.code = code;
        this.fileName = fileName;
        this.language = language;
        this.projectFiles = projectFiles;
        this.projectStructure = projectStructure;
        this.dependencies = dependencies;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<String, String> getProjectFiles() {
        return projectFiles;
    }

    public void setProjectFiles(Map<String, String> projectFiles) {
        this.projectFiles = projectFiles;
    }

    public List<String> getProjectStructure() {
        return projectStructure;
    }

    public void setProjectStructure(List<String> projectStructure) {
        this.projectStructure = projectStructure;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<String, String> dependencies) {
        this.dependencies = dependencies;
    }
}*/
// backend/src/main/java/com/codegen/model/response/GeneratedCodeResponse.java
package com.codegen.model.response;

import java.util.Map;
import java.util.List;

public class GeneratedCodeResponse {
    private String code;
    private String fileName;
    private String language;
    private Map<String, String> projectFiles;
    private List<String> projectStructure;
    private Map<String, String> dependencies;
    private String platform;
    private String pattern;
    private String status;
    private String error;

    // Constructor
    public GeneratedCodeResponse() {
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<String, String> getProjectFiles() {
        return projectFiles;
    }

    public void setProjectFiles(Map<String, String> projectFiles) {
        this.projectFiles = projectFiles;
    }

    public List<String> getProjectStructure() {
        return projectStructure;
    }

    public void setProjectStructure(List<String> projectStructure) {
        this.projectStructure = projectStructure;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<String, String> dependencies) {
        this.dependencies = dependencies;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}