package com.softarex.questionnaire.repos;

import com.softarex.questionnaire.models.QuizResult;
import org.springframework.data.repository.CrudRepository;

public interface QuizResultRepo extends CrudRepository<QuizResult, Long> {
}