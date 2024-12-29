package com.codegen.model.entity;

import java.util.List;
import java.util.ArrayList;

public class ParsedUMLModel {
    private List<UMLClass> classes;
    private List<UMLRelationship> relationships;

    public ParsedUMLModel() {
        this.classes = new ArrayList<>();
        this.relationships = new ArrayList<>();
    }

    public static class UMLClass {
        private String name;
        private List<UMLField> fields;

        public UMLClass(String name) {
            this.name = name;
            this.fields = new ArrayList<>();
        }

        public String getName() { return name; }
        public List<UMLField> getFields() { return fields; }
        public void addField(UMLField field) { fields.add(field); }
    }

    public static class UMLField {
        private String name;
        private String type;
        private boolean required;
        private boolean unique;

        public UMLField(String name, String type, boolean required, boolean unique) {
            this.name = name;
            this.type = type;
            this.required = required;
            this.unique = unique;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public boolean isRequired() { return required; }
        public boolean isUnique() { return unique; }
    }

    public static class UMLRelationship {
        private String sourceClass;
        private String targetClass;
        private String type;
        private String cardinality;

        public UMLRelationship(String sourceClass, String targetClass, String type, String cardinality) {
            this.sourceClass = sourceClass;
            this.targetClass = targetClass;
            this.type = type;
            this.cardinality = cardinality;
        }

        public String getSourceClass() { return sourceClass; }
        public String getTargetClass() { return targetClass; }
        public String getType() { return type; }
        public String getCardinality() { return cardinality; }
    }

    public void addClass(UMLClass umlClass) {
        classes.add(umlClass);
    }

    public void addRelationship(UMLRelationship relationship) {
        relationships.add(relationship);
    }

    public List<UMLClass> getClasses() { return classes; }
    public List<UMLRelationship> getRelationships() { return relationships; }
}