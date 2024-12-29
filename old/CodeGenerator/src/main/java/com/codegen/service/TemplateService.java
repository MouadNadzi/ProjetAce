package com.codegen.service;

import com.codegen.model.entity.Pattern;
import com.codegen.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @PostConstruct
    public void initializeTemplates() {
        // Initialize Android MVC template
        createTemplate(
                "mvc",
                "android",
                "Model-View-Controller pattern for Android",
                loadTemplateContent("templates/android/mvc/template.java")
        );

        // Initialize Android MVVM template
        createTemplate(
                "mvvm",
                "android",
                "Model-View-ViewModel pattern for Android",
                loadTemplateContent("templates/android/mvvm/template.java")
        );

        // Initialize iOS MVC template
        createTemplate(
                "mvc",
                "ios",
                "Model-View-Controller pattern for iOS",
                loadTemplateContent("templates/ios/mvc/template.swift")
        );

        // Initialize iOS MVVM template
        createTemplate(
                "mvvm",
                "ios",
                "Model-View-ViewModel pattern for iOS",
                loadTemplateContent("templates/ios/mvvm/template.swift")
        );
    }

    private void createTemplate(String name, String platform, String description, String content) {
        if (templateRepository.findByPlatformAndName(platform, name).isEmpty()) {
            Pattern pattern = new Pattern();
            pattern.setName(name);
            pattern.setPlatform(platform);
            pattern.setDescription(description);
            pattern.setTemplateContent(content);
            templateRepository.save(pattern);
        }
    }

    private String loadTemplateContent(String path) {
        try {
            Resource resource = new ClassPathResource(path);
            Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load template: " + path, e);
        }
    }
}