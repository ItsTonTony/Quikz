package com.echofyteam.backend.feature.auth.dto.response;

import com.echofyteam.backend.feature.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO representing the response returned after a successful sign-up.
 * Contains both authentication tokens and user information.
 *
 * @param signInResponse the sign-in response containing access and refresh tokens.
 * @param userResponse   the user details of the newly registered user.
 */
public record SignUpResponse(
        @Schema(description = "Sign-in response with tokens")
        SignInResponse signInResponse,

        @Schema(description = "User details of the newly registered user")
        UserResponse userResponse
) {
}
