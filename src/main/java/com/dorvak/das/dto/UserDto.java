package com.dorvak.das.dto;

import com.dorvak.das.models.User;

import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        String avatar) {

    public UserDto(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatarFile()
        );
    }
}
