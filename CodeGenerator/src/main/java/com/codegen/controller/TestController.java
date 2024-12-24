package com.codegen.controller;

import com.codegen.model.request.TestRequest;
import com.codegen.model.response.TestResponse;
import com.codegen.service.CodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @PostMapping("/mvvm/activity")
    public ResponseEntity<TestResponse> testMVVMActivity(@RequestBody TestRequest request) {
        String code = codeGeneratorService.generateMVVMActivity(
                request.getPackageName(),
                request.getProjectName()
        );
        return ResponseEntity.ok(new TestResponse("Activity.java", code));
    }

    @PostMapping("/mvvm/viewmodel")
    public ResponseEntity<TestResponse> testMVVMViewModel(@RequestBody TestRequest request) {
        String code = codeGeneratorService.generateMVVMViewModel(
                request.getPackageName(),
                request.getProjectName()
        );
        return ResponseEntity.ok(new TestResponse("ViewModel.java", code));
    }

    @PostMapping("/mvvm/model")
    public ResponseEntity<TestResponse> testMVVMModel(@RequestBody TestRequest request) {
        String code = codeGeneratorService.generateMVVMModel(
                request.getPackageName(),
                request.getProjectName()
        );
        return ResponseEntity.ok(new TestResponse("Model.java", code));
    }

    @PostMapping("/mvvm/repository")
    public ResponseEntity<TestResponse> testMVVMRepository(@RequestBody TestRequest request) {
        String code = codeGeneratorService.generateMVVMRepository(
                request.getPackageName(),
                request.getProjectName()
        );
        return ResponseEntity.ok(new TestResponse("Repository.java", code));
    }

    @PostMapping("/mvvm/layout")
    public ResponseEntity<TestResponse> testMVVMLayout(@RequestBody TestRequest request) {
        String layout = codeGeneratorService.generateMVVMLayout(request.getProjectName());
        return ResponseEntity.ok(new TestResponse("activity_main.xml", layout));
    }
}