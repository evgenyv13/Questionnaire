package com.softarex.questionnaire.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "fields")
public class Field {
    @Id
    @GeneratedValue
    private Long id;
    private String label;
    @Enumerated(EnumType.STRING)
    private FieldType type;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> options;
    private boolean required;
    private boolean isActive;

    public Field() {}

    public Field(String label, FieldType type, List<String> options, boolean required, boolean isActive) {
        this.label = label;
        this.type = type;
        if (type == FieldType.RADIOBUTTON || type == FieldType.COMBOBOX) this.options = options;
        this.required = required;
        this.isActive = isActive;
    }

    public Long getId() { return id; }

    public String getLabel() { return label; }

    public void setLabel(String label) { this.label = label; }

    public FieldType getType() { return type; }

    public List<String> getOptions() { return options; }

    public void setOptions(List<String> options) {
        if (type == FieldType.RADIOBUTTON || type == FieldType.COMBOBOX) this.options = options;
    }

    public void setType(FieldType type) { this.type = type; }

    public boolean isRequired() { return required; }

    public void setRequired(boolean required) { this.required = required; }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }
}