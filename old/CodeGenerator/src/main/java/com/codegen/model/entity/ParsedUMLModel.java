// codegen-service/src/main/java/com/example/codegen/model/ParsedUMLModel.java
package com.codegen.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the parsed UML structure from the diagram.
 */
public class ParsedUMLModel {

    private List<UMLClass> classes = new ArrayList<>();

    public List<UMLClass> getClasses() {
        return classes;
    }

    public void setClasses(List<UMLClass> classes) {
        this.classes = classes;
    }

    public static class UMLClass {
        private String name;
        private List<UMLField> fields;
        private List<UMLRelationship> relationships;

        public UMLClass() { }

        public UMLClass(String name, List<UMLField> fields, List<UMLRelationship> relationships) {
            this.name = name;
            this.fields = fields;
            this.relationships = relationships;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<UMLField> getFields() { return fields; }
        public void setFields(List<UMLField> fields) { this.fields = fields; }

        public List<UMLRelationship> getRelationships() { return relationships; }
        public void setRelationships(List<UMLRelationship> relationships) {
            this.relationships = relationships;
        }
    }

    public static class UMLField {
        private String name;
        private String type; // e.g. "String", "int", etc.

        public UMLField() {}
        public UMLField(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    public static class UMLRelationship {
        private String type;   // e.g. "OneToMany"
        private String target; // e.g. "OtherClass"

        public UMLRelationship() {}
        public UMLRelationship(String type, String target) {
            this.type = type;
            this.target = target;
        }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getTarget() { return target; }
        public void setTarget(String target) { this.target = target; }
    }
}
