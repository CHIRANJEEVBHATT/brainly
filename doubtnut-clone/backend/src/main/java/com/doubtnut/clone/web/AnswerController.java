package com.doubtnut.clone.web;

import com.doubtnut.clone.model.Answer;
import com.doubtnut.clone.model.User;
import com.doubtnut.clone.repository.UserRepository;
import com.doubtnut.clone.service.AnswerService;
import com.doubtnut.clone.web.dto.AnswerDtos.CreateAnswerRequest;
import com.doubtnut.clone.web.dto.AnswerDtos.AnswerResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    private final AnswerService answerService;
    private final UserRepository userRepository;

    public AnswerController(AnswerService answerService, UserRepository userRepository) {
        this.answerService = answerService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public AnswerResponse create(@Valid @RequestBody CreateAnswerRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow();
        Answer a = answerService.create(request.getQuestionId(), request.getContent(), user);
        AnswerResponse r = new AnswerResponse();
        r.setId(a.getId());
        r.setContent(a.getContent());
        r.setUserName(a.getUser() != null ? a.getUser().getName() : null);
        r.setCreatedAt(a.getCreatedAt());
        return r;
    }
}


