package com.doubtnut.clone.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class AnswerDtos {
    @Data
    public static class CreateAnswerRequest {
        @NotNull
        private Long questionId;
        @NotBlank
        private String content;
        @NotNull
        private Long userId;
    }

    @Data
    public static class AnswerResponse {
        private Long id;
        private String content;
        private String userName;
        private Instant createdAt;
    }
}


