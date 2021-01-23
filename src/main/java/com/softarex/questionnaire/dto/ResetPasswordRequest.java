package com.softarex.questionnaire.dto;

public class ResetPasswordRequest {
    private String email;
    private String code;

    public ResetPasswordRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public String getEmail() { return email; }

    public String getCode() { return code; }
}