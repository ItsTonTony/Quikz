package com.echofyteam.backend.feature.user.dto.request;

import lombok.Builder;

@Builder
public record CreateUserRequest(
        String email,
        String password
) {
}
