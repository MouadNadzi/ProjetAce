package com.codegen.model.response;

public class TestResponse {
    private String fileName;
    private String code;

    public TestResponse(String fileName, String code) {
        this.fileName = fileName;
        this.code = code;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCode() {
        return code;
    }
}