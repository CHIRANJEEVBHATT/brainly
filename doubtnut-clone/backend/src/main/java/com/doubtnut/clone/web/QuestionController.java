package com.doubtnut.clone.web;

import com.doubtnut.clone.model.Question;
import com.doubtnut.clone.model.User;
import com.doubtnut.clone.repository.UserRepository;
import com.doubtnut.clone.service.QuestionService;
import com.doubtnut.clone.web.dto.QuestionDtos.CreateQuestionRequest;
import com.doubtnut.clone.web.dto.QuestionDtos.QuestionResponse;
import com.doubtnut.clone.web.dto.QuestionDtos.AnswerResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final UserRepository userRepository;

    public QuestionController(QuestionService questionService, UserRepository userRepository) {
        this.questionService = questionService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<QuestionResponse> all() {
        return questionService.getAll().stream().map(q -> {
            QuestionResponse r = new QuestionResponse();
            r.setId(q.getId());
            r.setTitle(q.getTitle());
            r.setDescription(q.getDescription());
            r.setUserName(q.getUser() != null ? q.getUser().getName() : null);
            r.setCreatedAt(q.getCreatedAt());
            List<AnswerResponse> answers = q.getAnswers().stream().map(a -> {
                AnswerResponse ar = new AnswerResponse();
                ar.setId(a.getId());
                ar.setContent(a.getContent());
                ar.setUserName(a.getUser() != null ? a.getUser().getName() : null);
                ar.setCreatedAt(a.getCreatedAt());
                return ar;
            }).collect(Collectors.toList());
            r.setAnswers(answers);
            return r;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public QuestionResponse create(@Valid @RequestBody CreateQuestionRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow();
        Question q = questionService.create(request.getTitle(), request.getDescription(), user);
        QuestionResponse r = new QuestionResponse();
        r.setId(q.getId());
        r.setTitle(q.getTitle());
        r.setDescription(q.getDescription());
        r.setUserName(q.getUser() != null ? q.getUser().getName() : null);
        r.setCreatedAt(q.getCreatedAt());
        r.setAnswers(List.of());
        return r;
    }
}


