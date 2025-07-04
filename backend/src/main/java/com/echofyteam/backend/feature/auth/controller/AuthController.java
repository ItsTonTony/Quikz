package com.echofyteam.backend.feature.auth.controller;

import com.echofyteam.backend.feature.auth.dto.request.SignInRequest;
import com.echofyteam.backend.feature.auth.dto.request.SignUpRequest;
import com.echofyteam.backend.feature.auth.dto.response.RefreshTokenResponse;
import com.echofyteam.backend.feature.auth.dto.response.SignInResponse;
import com.echofyteam.backend.feature.auth.dto.response.SignUpResponse;
import com.echofyteam.backend.feature.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "API for user authentication and management")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "User Sign In",
            description = "Authenticate user by email or username and password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful authentication",
                            content = @Content(schema = @Schema(implementation = SignInResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials or validation error")
            }
    )
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(
            @Parameter(description = "Sign in request payload", required = true)
            @RequestBody @Valid SignInRequest signInRequest) {
        SignInResponse signInResponse = authService.signIn(signInRequest);
        return ResponseEntity.ok(signInResponse);
    }

    @Operation(
            summary = "User Sign Up",
            description = "Register a new user with email and password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful registration",
                            content = @Content(schema = @Schema(implementation = SignUpResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error or passwords do not match")
            }
    )
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(
            @Parameter(description = "Sign up request payload", required = true)
            @RequestBody @Valid SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = authService.signUp(signUpRequest);
        return ResponseEntity.ok(signUpResponse);
    }

    @Operation(
            summary = "Refresh Access Token",
            description = "Generate new access and refresh tokens using a valid refresh token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully",
                            content = @Content(schema = @Schema(implementation = RefreshTokenResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired refresh token")
            }
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @Parameter(description = "Refresh token string", required = true)
            @RequestParam("refresh-token") String refreshToken) {
        RefreshTokenResponse refreshTokenResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(refreshTokenResponse);
    }

    @Operation(
            summary = "User Sign Out",
            description = "Invalidate the given refresh token to sign out the user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sign out successful"),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired refresh token")
            }
    )
    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(
            @Parameter(description = "Refresh token string", required = true)
            @RequestParam("refresh-token") String refreshToken) {
        authService.signOut(refreshToken);
        return ResponseEntity.ok().build();
    }
}
