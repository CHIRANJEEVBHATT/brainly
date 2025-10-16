package com.doubtnut.clone.service;

import com.doubtnut.clone.model.User;
import com.doubtnut.clone.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User upsertByEmail(String name, String email, String pictureUrl) {
        return userRepository.findByEmail(email)
                .map(existing -> {
                    existing.setName(name);
                    existing.setPictureUrl(pictureUrl);
                    return userRepository.save(existing);
                })
                .orElseGet(() -> userRepository.save(User.builder()
                        .name(name)
                        .email(email)
                        .pictureUrl(pictureUrl)
                        .build()));
    }
}


