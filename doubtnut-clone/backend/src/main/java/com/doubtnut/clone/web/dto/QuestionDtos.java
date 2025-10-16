package com.doubtnut.clone.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class QuestionDtos {
    @Data
    public static class CreateQuestionRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String description;
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

    @Data
    public static class QuestionResponse {
        private Long id;
        private String title;
        private String description;
        private String userName;
        private Instant createdAt;
        private List<AnswerResponse> answers;
    }
}


