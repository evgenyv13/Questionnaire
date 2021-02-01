package com.softarex.questionnaire.dto.responses;

import com.softarex.questionnaire.models.Field;

import java.util.List;

public class FieldList extends ResponseWrapper {
    private List<Field> fields;

    public FieldList(List<Field> fields) {
        super("OK", null);
        this.fields = fields;
    }

    public List<Field> getFields() { return fields; }
}