package com.echofyteam.backend.feature.user.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String username,
        Instant lastLogin
) {
}
