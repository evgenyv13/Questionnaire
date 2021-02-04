package com.softarex.questionnaire.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softarex.questionnaire.models.Field;

import java.util.ArrayList;
import java.util.List;

public class QuizList extends ResponseWrapper {
    private List<QuizDetails> quizzes = new ArrayList<>();

    public QuizList() {
        super("OK", null);
    }

    public void add(Long userId, List<Field> fields) { quizzes.add(new QuizDetails(userId, fields)); }

    public List<QuizDetails> getQuizzes() { return quizzes; }

    public class QuizDetails {
        @JsonProperty("user-id")
        private Long userId;
        @JsonIgnoreProperties("active")
        private List<Field> fields;

        public QuizDetails(Long userId, List<Field> fields) {
            this.userId = userId;
            this.fields = fields;
        }

        public Long getUserId() { return userId; }

        public List<Field> getFields() { return fields; }
    }
}