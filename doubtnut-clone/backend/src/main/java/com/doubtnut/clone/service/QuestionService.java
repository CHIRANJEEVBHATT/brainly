package com.doubtnut.clone.service;

import com.doubtnut.clone.model.Question;
import com.doubtnut.clone.model.User;
import com.doubtnut.clone.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getAll() {
        return questionRepository.findAllByOrderByCreatedAtDesc();
    }

    public Question create(String title, String description, User user) {
        Question q = Question.builder()
                .title(title)
                .description(description)
                .user(user)
                .createdAt(Instant.now())
                .build();
        return questionRepository.save(q);
    }
}


