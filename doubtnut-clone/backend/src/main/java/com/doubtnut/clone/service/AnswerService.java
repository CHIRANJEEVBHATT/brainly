package com.doubtnut.clone.service;

import com.doubtnut.clone.model.Answer;
import com.doubtnut.clone.model.Question;
import com.doubtnut.clone.model.User;
import com.doubtnut.clone.repository.AnswerRepository;
import com.doubtnut.clone.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public AnswerService(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    public Answer create(Long questionId, String content, User user) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));
        Answer answer = Answer.builder()
                .question(question)
                .content(content)
                .user(user)
                .createdAt(Instant.now())
                .build();
        return answerRepository.save(answer);
    }
}


