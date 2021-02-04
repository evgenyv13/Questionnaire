package com.softarex.questionnaire.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class QuizResultDTO {
    private Long userId;
    private Map<Long, String> answers;

    @JsonCreator
    public QuizResultDTO(@JsonProperty("user-id") Long userId, Map<Long, String> answers) {
        this.userId = userId;
        this.answers = answers;
    }

    public Long getUserId() { return userId; }

    public Map<Long, String> getAnswers() { return answers; }
}