package com.doubtnut.clone.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthDtos {
    @Data
    public static class GoogleAuthRequest {
        @NotBlank
        private String name;
        @NotBlank
        @Email
        private String email;
        private String pictureUrl;
    }

    @Data
    public static class UserResponse {
        private Long id;
        private String name;
        private String email;
        private String pictureUrl;
    }
}


