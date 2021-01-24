package com.softarex.questionnaire.dto;

public class PasswordResetRequest {
    private String email;
    private String code;

    public PasswordResetRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public String getEmail() { return email; }

    public String getCode() { return code; }
}