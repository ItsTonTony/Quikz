package com.echofyteam.backend.feature.user.dto.request;

import lombok.Builder;

import java.util.Set;

@Builder
public record CreateUserRequest(
        String email,
        String password,
        Set<String> roles
) {
}
