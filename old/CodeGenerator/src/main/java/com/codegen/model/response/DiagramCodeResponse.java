// backend/src/main/java/com/codegen/model/response/DiagramCodeResponse.java
package com.codegen.model.response;

import java.util.HashMap;
import java.util.List;

public class DiagramCodeResponse {
    private String platform;
    private String architecture;
    private String status;
    private String error;
    private HashMap<String, List<HashMap<String, String>>> generatedFiles;

    // Getters and Setters
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
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

    public HashMap<String, List<HashMap<String, String>>> getGeneratedFiles() {
        return generatedFiles;
    }

    public void setGeneratedFiles(HashMap<String, List<HashMap<String, String>>> generatedFiles) {
        this.generatedFiles = generatedFiles;
    }
}