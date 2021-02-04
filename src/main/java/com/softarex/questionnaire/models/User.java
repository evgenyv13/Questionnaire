package com.softarex.questionnaire.models;

import com.softarex.questionnaire.dto.responses.FieldList;
import com.softarex.questionnaire.dto.responses.ProfileInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<Field> fields = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<QuizResult> results = new ArrayList<>();

    public User() {}

    public User(String email, String passwordHash, String firstName, String lastName, String phoneNumber) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Field> getFields() { return fields; }

    public void addField(Field field) { fields.add(field); }

    public boolean editField(Long id, String label, FieldType type, List<String> options, boolean required, boolean isActive) {
        Optional<Field> fieldOpt = fields.stream().filter(el -> el.getId().equals(id)).findFirst();
        if (fieldOpt.isEmpty()) return false;
        Field field = fieldOpt.get();
        field.setLabel(label);
        field.setType(type);
        field.setOptions(options);
        field.setRequired(required);
        field.setActive(isActive);
        return true;
    }

    public boolean deleteFieldById(Long id) { return fields.removeIf(el -> el.getId().equals(id)); }

    public void addResult(QuizResult result) { results.add(result); }

    public ProfileInfo getProfileInfo() { return new ProfileInfo(firstName, lastName, email, phoneNumber); }

    public FieldList getFiledList() { return new FieldList(fields); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() { return getPasswordHash(); }

    @Override
    public String getUsername() { return getEmail(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}