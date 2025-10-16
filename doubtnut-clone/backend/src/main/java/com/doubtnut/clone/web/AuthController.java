package com.doubtnut.clone.web;

import com.doubtnut.clone.model.User;
import com.doubtnut.clone.service.UserService;
import com.doubtnut.clone.web.dto.AuthDtos.GoogleAuthRequest;
import com.doubtnut.clone.web.dto.AuthDtos.UserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/google")
    public UserResponse google(@Valid @RequestBody GoogleAuthRequest request) {
        User user = userService.upsertByEmail(request.getName(), request.getEmail(), request.getPictureUrl());
        UserResponse resp = new UserResponse();
        resp.setId(user.getId());
        resp.setName(user.getName());
        resp.setEmail(user.getEmail());
        resp.setPictureUrl(user.getPictureUrl());
        return resp;
    }
}


