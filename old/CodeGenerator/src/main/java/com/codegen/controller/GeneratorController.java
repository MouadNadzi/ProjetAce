/*package com.codegen.controller;

import com.codegen.model.request.GenerationRequest;
import com.codegen.model.response.GeneratedCodeResponse;
import com.codegen.service.CodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/generator")  // Remove /api since it's in application.properties
@CrossOrigin(origins = "*")
public class GeneratorController {

    private final CodeGeneratorService generatorService;

    @Autowired
    public GeneratorController(CodeGeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Generator Controller is working!");
    }

    @PostMapping("/generate")
    public ResponseEntity<GeneratedCodeResponse> generateCode(@Valid @RequestBody GenerationRequest request) {
        try {
            GeneratedCodeResponse response = generatorService.generateCode(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}*/
/*
package com.codegen.controller;

import com.codegen.model.request.GenerationRequest;
import com.codegen.model.response.GeneratedCodeResponse;
import com.codegen.service.CodeGeneratorService;
import com.codegen.service.ProjectStructureGenerator;
import com.codegen.service.ProjectStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/generator")
@CrossOrigin(origins = "*")
public class GeneratorController {

    private final CodeGeneratorService codeGeneratorService;
    private final ProjectStructureGenerator projectStructureGenerator;
    private final ProjectStructureService projectStructureService;

    @Autowired
    public GeneratorController(
            CodeGeneratorService codeGeneratorService,
            ProjectStructureGenerator projectStructureGenerator,
            ProjectStructureService projectStructureService) {
        this.codeGeneratorService = codeGeneratorService;
        this.projectStructureGenerator = projectStructureGenerator;
        this.projectStructureService = projectStructureService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GeneratedCodeResponse> generateCode(@Valid @RequestBody GenerationRequest request) {
        String mainCode = codeGeneratorService.generateCode(request);
        String fileName = request.getProjectName() + "Activity.java";
        String language = "java";

        // Get project structure
        List<String> projectStructure = projectStructureGenerator.generateProjectStructure(
                request.getPlatform(),
                request.getPattern()
        );

        // Get project files
        Map<String, String> projectFiles = projectStructureService.generateAndroidMVVMStructure(
                request.getPackageName(),
                request.getProjectName()
        );

        // Get dependencies
        Map<String, String> dependencies = projectStructureGenerator.getDependencies(
                request.getPlatform(),
                request.getPattern()
        );

        GeneratedCodeResponse response = new GeneratedCodeResponse(
                mainCode,
                fileName,
                language,
                projectFiles,
                projectStructure,
                dependencies
        );

        return ResponseEntity.ok(response);
    }
}*/

/*
package com.codegen.controller;

import com.codegen.model.request.GenerationRequest;
import com.codegen.model.response.GeneratedCodeResponse;
import com.codegen.service.CodeGeneratorService;
import com.codegen.service.ProjectStructureGenerator;
import com.codegen.service.ProjectStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/generator")
@CrossOrigin(origins = "*")
public class GeneratorController {

    private final CodeGeneratorService codeGeneratorService;
    private final ProjectStructureGenerator projectStructureGenerator;
    private final ProjectStructureService projectStructureService;

    @Autowired
    public GeneratorController(
            CodeGeneratorService codeGeneratorService,
            ProjectStructureGenerator projectStructureGenerator,
            ProjectStructureService projectStructureService) {
        this.codeGeneratorService = codeGeneratorService;
        this.projectStructureGenerator = projectStructureGenerator;
        this.projectStructureService = projectStructureService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GeneratedCodeResponse> generateCode(@Valid @RequestBody GenerationRequest request) {
        String mainCode = codeGeneratorService.generateCode(request);
        String fileName = request.getProjectName() + "Activity.java";
        String language = "java";

        // Get project structure
        List<String> projectStructure = projectStructureGenerator.generateProjectStructure(
                request.getPlatform(),
                request.getPattern()
        );

        // Get project files
        Map<String, String> projectFiles = projectStructureService.generateProjectStructure(
                request.getPackageName(),
                request.getProjectName(),
                request.getPlatform(),
                request.getPattern()
        );

        // Get dependencies
        Map<String, String> dependencies = projectStructureGenerator.getDependencies(
                request.getPlatform(),
                request.getPattern()
        );

        GeneratedCodeResponse response = new GeneratedCodeResponse(
                mainCode,
                fileName,
                language,
                projectFiles,
                projectStructure,
                dependencies
        );

        return ResponseEntity.ok(response);
    }
}*/
// backend/src/main/java/com/codegen/controller/GeneratorController.java
// backend/src/main/java/com/codegen/controller/GeneratorController.java
package com.codegen.controller;

import com.codegen.model.request.GenerationRequest;
import com.codegen.model.response.GeneratedCodeResponse;
import com.codegen.model.response.DiagramCodeResponse;
import com.codegen.service.CodeGeneratorService;
import com.codegen.service.DiagramProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/generator")
@CrossOrigin(origins = "*")
public class GeneratorController {

    private final CodeGeneratorService codeGeneratorService;
    private final DiagramProcessorService diagramProcessorService;

    @Autowired
    public GeneratorController(CodeGeneratorService codeGeneratorService,
                               DiagramProcessorService diagramProcessorService) {
        this.codeGeneratorService = codeGeneratorService;
        this.diagramProcessorService = diagramProcessorService;
    }

    /*@PostMapping("/generate")
    public ResponseEntity<GeneratedCodeResponse> generateCode(@RequestBody GenerationRequest request) {
        return ResponseEntity.ok(codeGeneratorService.generateCode(request));
    }*/


    @PostMapping("/generate/diagram")
    public ResponseEntity<DiagramCodeResponse> generateFromDiagram(
            @RequestParam("file") MultipartFile file,
            @RequestParam("platform") String platform,
            @RequestParam("architecture") String architecture) {
        try {
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            HashMap<String, List<HashMap<String, String>>> generatedCode =
                    diagramProcessorService.processDiagram(base64Image, platform, architecture);

            DiagramCodeResponse response = new DiagramCodeResponse();
            response.setGeneratedFiles(generatedCode);
            response.setPlatform(platform);
            response.setArchitecture(architecture);
            response.setStatus("SUCCESS");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DiagramCodeResponse errorResponse = new DiagramCodeResponse();
            errorResponse.setStatus("ERROR");
            errorResponse.setError(e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}