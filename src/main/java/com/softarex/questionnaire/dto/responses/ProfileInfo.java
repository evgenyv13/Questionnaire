package com.softarex.questionnaire.dto.responses;

public class ProfileInfo extends ResponseWrapper {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public ProfileInfo(String firstName, String lastName, String email, String phoneNumber) {
        super("OK", null);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getEmail() { return email; }

    public String getPhoneNumber() { return phoneNumber; }
}