package com.dorvak.das.dto;

import java.util.List;

public record TokenDto(List<String> scopes, String clientId, String redirectUri, String password, String username) {
}
