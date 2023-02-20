package com.dorvak.das.dto;

import java.util.List;

public record Oauth2ApplicationDto(List<String> scopes, String clientId, String redirectUri, String name) {
}
