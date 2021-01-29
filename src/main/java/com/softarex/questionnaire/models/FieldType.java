package com.softarex.questionnaire.models;

public enum FieldType {
    SINGLE_LINE_TEXT("Single line text"),
    MULTI_LINE_TEXT("Multi line text"),
    RADIOBUTTON("Radio button"),
    CHECKBOX("Check box"),
    COMBOBOX("Combobox"),
    DATE("Date");

    private String stringView;

    FieldType(String stringView) { this.stringView = stringView; }

    @Override
    public String toString() { return stringView; }
}