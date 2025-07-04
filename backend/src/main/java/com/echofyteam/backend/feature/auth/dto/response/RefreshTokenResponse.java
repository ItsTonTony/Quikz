package com.echofyteam.backend.feature.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO representing the response containing new access and refresh tokens.
 *
 * @param accessToken  the new access token used for authenticating API requests.
 * @param refreshToken the new refresh token used to obtain future access tokens.
 */
public record RefreshTokenResponse(
        @Schema(description = "New access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "New refresh token", example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...")
        String refreshToken
) {
}
