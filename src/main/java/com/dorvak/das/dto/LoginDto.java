package com.dorvak.das.dto;

public record LoginDto(String username, String password, String email, String language, String oldPassword) {
}
