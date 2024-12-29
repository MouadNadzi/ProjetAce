// codegen-service/src/main/java/com/example/codegen/controller/MobileCodegenController.java
package com.codegen.controller;

import com.codegen.model.request.GenerationRequest;
import com.codegen.model.entity.ParsedUMLModel;
import com.codegen.service.AIService;
import com.codegen.service.CodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/mobile-codegen")
public class MobileCodegenController {

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Autowired
    private AIService aiService;

    /**
     * Generate skeleton code (MVC/MVVM/MVP) for Android/iOS (no UML).
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateMobileApp(@RequestBody GenerationRequest request) {
        try {
            String generated = codeGeneratorService.generateCode(request);
            return ResponseEntity.ok(generated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Generate code from UML diagram (image) + user inputs (platform/pattern/packageName/projectName).
     */
    @PostMapping(value = "/generate/from-diagram", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> generateFromDiagram(
            @RequestParam("platform") String platform,
            @RequestParam("pattern") String pattern,
            @RequestParam("packageName") String packageName,
            @RequestParam("projectName") String projectName,
            @RequestParam("diagramImage") MultipartFile diagramImage
    ) {
        try {
            // 1) Convert image to Base64
            String base64Image = Base64.getEncoder().encodeToString(diagramImage.getBytes());

            // 2) Parse UML from AI
            ParsedUMLModel umlModel = aiService.parseDiagram(base64Image);

            // 3) Generate code from UML
            Map<String, String> files = codeGeneratorService.generateFromUML(
                    platform,
                    pattern,
                    packageName,
                    projectName,
                    umlModel
            );

            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

}
