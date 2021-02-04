package com.softarex.questionnaire.models;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "results")
public class QuizResult {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;
    @ElementCollection
    @MapKeyColumn(name = "field_id")
    @Column(name = "answer")
    private Map<Long, String> answers;

    public QuizResult() {}

    public QuizResult(User user, Map<Long, String> answers) {
        this.user = user;
        this.answers = answers;
    }

    public Long getId() { return id; }

    public User getUser() { return user; }

    public Map<Long, String> getAnswers() { return answers; }
}