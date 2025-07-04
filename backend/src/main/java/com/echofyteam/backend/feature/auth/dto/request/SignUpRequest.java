package com.echofyteam.backend.feature.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * DTO for sign-up request
 *
 * @param email the email address, must not be empty and must be a valid email format.
 * @param password the password, must not be empty and must be at least 8 characters long.
 * @param confirmPassword the repeated password; must match the password and must not be empty.
 */
@Builder
public record SignUpRequest(
        @NotEmpty(message = "Email must not be empty")
        @Email
        @Schema(description = "User's email address", example = "user@example.com", required = true)
        String email,

        @NotEmpty(message = "Password must not be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Schema(description = "Password (minimum 8 characters)", example = "P@ssw0rd123", required = true)
        String password,

        @NotEmpty(message = "Confirm password must not be empty")
        @Schema(description = "Password confirmation; must match password", example = "P@ssw0rd123", required = true)
        String confirmPassword
) {
}
