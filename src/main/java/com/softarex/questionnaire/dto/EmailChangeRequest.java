package com.softarex.questionnaire.dto;

public class EmailChangeRequest {
    private String oldEmail;
    private String newEmail;
    private String code;

    public EmailChangeRequest(String oldEmail, String newEmail, String code) {
        this.oldEmail = oldEmail;
        this.newEmail = newEmail;
        this.code = code;
    }

    public String getOldEmail() { return oldEmail; }

    public String getNewEmail() { return newEmail; }

    public String getCode() { return code; }
}