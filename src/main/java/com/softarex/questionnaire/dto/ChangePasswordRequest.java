package com.softarex.questionnaire.dto;

public class ChangePasswordRequest {
    private String email;
    private String code;
    private String newPassword;

    public ChangePasswordRequest(String email, String code, String newPassword) {
        this.email = email;
        this.code = code;
        this.newPassword = newPassword;
    }

    public String getEmail() { return email; }

    public String getCode() { return code; }

    public String getNewPassword() { return newPassword; }
}