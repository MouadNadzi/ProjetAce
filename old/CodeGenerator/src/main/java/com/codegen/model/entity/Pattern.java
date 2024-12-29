package com.codegen.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "patterns")
public class Pattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String platform;
    private String description;

    @Column(columnDefinition = "TEXT")
    private String templateContent;

    // Default constructor
    public Pattern() {}

    // Constructor with all fields
    public Pattern(Long id, String name, String platform, String description, String templateContent) {
        this.id = id;
        this.name = name;
        this.platform = platform;
        this.description = description;
        this.templateContent = templateContent;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlatform() {
        return platform;
    }

    public String getDescription() {
        return description;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }
}