package com.dorvak.das.dto;

import com.dorvak.das.models.User;

import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        String avatar,
        String language) {

    public UserDto(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                getAvatarUrl(user),
                user.getLanguage()
        );
    }

    public static String getAvatarUrl(User user) {
        return "/api/user/avatar/" + user.getId() + "/" + user.getAvatarFile();
    }
}
