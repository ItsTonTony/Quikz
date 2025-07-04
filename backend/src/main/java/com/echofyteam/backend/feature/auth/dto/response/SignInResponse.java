package com.echofyteam.backend.feature.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO representing the response returned after a successful sign-in.
 *
 * @param accessToken  the JWT or access token used for authenticating API requests.
 * @param refreshToken the refresh token used to obtain new access tokens.
 */
public record SignInResponse(
        @Schema(description = "Access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "Refresh token", example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...")
        String refreshToken
) {
}
