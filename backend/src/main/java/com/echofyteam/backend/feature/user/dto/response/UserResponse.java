package com.echofyteam.backend.feature.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO representing user information returned in responses.
 *
 * @param id        the unique identifier of the user
 * @param username  the username of the user
 * @param lastLogin the timestamp of the user's last login
 */
@Builder
public record UserResponse(
        @Schema(description = "Unique identifier of the user", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Username of the user", example = "john_doe")
        String username,

        @Schema(description = "Timestamp of the last login", example = "2025-07-04T06:30:00Z")
        Instant lastLogin
) {
}
