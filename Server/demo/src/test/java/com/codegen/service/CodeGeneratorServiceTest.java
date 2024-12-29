// CodeGeneratorServiceTest.java
package com.codegen.service;

import com.codegen.model.entity.ParsedUMLModel;
import com.codegen.model.request.GenerationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CodeGeneratorServiceTest {

    @Mock
    private ProjectStructureGenerator structureGenerator;

    @Mock
    private GeminiCodeGeneratorService geminiService;

    @InjectMocks
    private CodeGeneratorService codeGeneratorService;

    private static final String TEST_UML = """
        model User {
            id: number required
            name: text required
            email: text required unique
            created_at: date
        }
        """;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateFromUML_ShouldGenerateAndroidMVVMFiles() {
        // Arrange
        ParsedUMLModel model = new ParsedUMLModel();
        // Set up model
        ParsedUMLModel.UMLClass userClass = new ParsedUMLModel.UMLClass("User");
        userClass.addField(new ParsedUMLModel.UMLField("id", "number", true, false));
        model.addClass(userClass);

        // Mock service responses
        when(geminiService.generateProject(any(), any(), any(), any(), any()))
                .thenReturn(Map.of("app/src/main/java/com/example/model/User.java", "// Generated code"));

        // Act
        Map<String, String> result = codeGeneratorService.generateFromUML(
                "android",
                "mvvm",
                "com.example",
                "TestApp",
                model
        );

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("app/src/main/java/com/example/model/User.java"));
        assertTrue(result.containsKey("app/build.gradle"));
    }

    @Test
    void generateFromUML_ShouldGenerateIOSMVVMFiles() {
        // Similar test for iOS MVVM
    }

    @Test
    void generateFromUML_ShouldHandleInvalidPlatform() {
        // Test invalid platform handling
    }
}