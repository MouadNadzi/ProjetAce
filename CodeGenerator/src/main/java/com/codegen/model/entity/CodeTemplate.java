package com.codegen.model.entity;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class CodeTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String language;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private Pattern pattern;
}
