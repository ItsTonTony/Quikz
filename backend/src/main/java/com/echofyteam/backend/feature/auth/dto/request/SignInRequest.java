package com.echofyteam.backend.feature.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

/**
 * DTO for sign-in request.
 *
 * @param emailOrUsername the email address or username; must not be empty.
 * @param password the password; must not be empty.
 */
@Builder
public record SignInRequest(
        @NotEmpty(message = "Username or email must not be empty")
        @Schema(description = "Email address or username of the user", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String emailOrUsername,

        @NotEmpty(message = "Password must not be empty")
        @Schema(description = "Password of the user", example = "P@ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}
