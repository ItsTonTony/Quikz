package com.echofyteam.backend.feature.auth.service.impl;

import com.echofyteam.backend.exception.impl.BusinessException;
import com.echofyteam.backend.exception.impl.BusinessExceptionReason;
import com.echofyteam.backend.feature.auth.dto.request.SignInRequest;
import com.echofyteam.backend.feature.auth.dto.request.SignUpRequest;
import com.echofyteam.backend.feature.auth.dto.response.RefreshTokenResponse;
import com.echofyteam.backend.feature.auth.dto.response.SignInResponse;
import com.echofyteam.backend.feature.auth.dto.response.SignUpResponse;
import com.echofyteam.backend.feature.auth.entity.RefreshTokenEntity;
import com.echofyteam.backend.feature.auth.repository.RefreshTokenRepository;
import com.echofyteam.backend.feature.auth.service.AuthService;
import com.echofyteam.backend.feature.role.entity.Role;
import com.echofyteam.backend.feature.user.dto.request.CreateUserRequest;
import com.echofyteam.backend.feature.user.dto.response.UserResponse;
import com.echofyteam.backend.feature.user.entity.UserEntity;
import com.echofyteam.backend.feature.user.repository.UserRepository;
import com.echofyteam.backend.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) {
        log.info("Sign in for user: {}", signInRequest.emailOrUsername());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.emailOrUsername(), signInRequest.password()
        ));

        UserEntity user = userRepository.findByEmail(signInRequest.emailOrUsername())
                .or(() -> userRepository.findByUsername(signInRequest.emailOrUsername()))
                .orElseThrow(() -> {
                    log.warn("User with credentials: {} not found", signInRequest.emailOrUsername());
                    return new BusinessException(BusinessExceptionReason.USER_NOT_FOUND);
                });

        user.setLastLoginAt(Instant.now());
        userRepository.save(user);

        log.info("Generate access and refresh tokens for user with id: {}", user.getId());
        Pair<String, String> tokens = getTokensForUser(user);
        return new SignInResponse(tokens.getFirst(), tokens.getSecond());
    }

    @Override
    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        log.info("Sign up for user: {}", signUpRequest.email());

        if (!signUpRequest.password().equals(signUpRequest.confirmPassword())) {
            log.warn("Passwords do not match for user: {}", signUpRequest.email());
            throw new BusinessException(BusinessExceptionReason.PASSWORDS_DO_NOT_MATCH);
        }

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email(signUpRequest.email())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .roles(Set.of("USER"))
                .build();

        log.info("Creating user: {}", signUpRequest.email());
        UserResponse userResponse = userService.createUser(createUserRequest);

        UserEntity user = userRepository.findById(userResponse.id())
                .orElseThrow(() -> {
                    log.warn("User with id: {} not found", userResponse.id());
                    return new BusinessException(BusinessExceptionReason.USER_NOT_FOUND);
                });

        log.info("Generate access and refresh tokens for user with id: {}", user.getId());
        Pair<String, String> tokens = getTokensForUser(user);

        return new SignUpResponse(
                new SignInResponse(tokens.getFirst(), tokens.getSecond()),
                userResponse
        );
    }

    @Override
    @Transactional
    public void signOut(String refreshToken) {
        log.info("Sign out for refresh token: {}", refreshToken);

        RefreshTokenEntity oldToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> {
                    log.warn("Refresh token with token: {} not found", refreshToken);
                    return new BusinessException(BusinessExceptionReason.TOKEN_NOT_FOUND);
                });

        if (oldToken.isRevoked() || oldToken.getExpiresAt().isBefore(Instant.now())) {
            log.warn("Refresh token is invalid");
            throw new BusinessException(BusinessExceptionReason.INVALID_TOKEN);
        }

        log.info("Refresh token set to revoked");
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {
        log.info("Refreshing token: {}", refreshToken);

        RefreshTokenEntity oldToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> {
                    log.warn("Refresh token with token: {} not found", refreshToken);
                    return new BusinessException(BusinessExceptionReason.TOKEN_NOT_FOUND);
                });

        if (oldToken.isRevoked() || oldToken.getExpiresAt().isBefore(Instant.now())) {
            log.warn("Refresh token is invalid");
            throw new BusinessException(BusinessExceptionReason.INVALID_TOKEN);
        }

        UserEntity user = oldToken.getUser();

        log.info("Old refresh token set to revoked");
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        log.info("Generate new access and refresh tokens for user with id: {}", user.getId());
        Pair<String, String> tokens = getTokensForUser(user);
        return new RefreshTokenResponse(tokens.getFirst(), tokens.getSecond());
    }

    private Pair<String, String> getTokensForUser(UserEntity user) {
        Map<String, Object> claims = Map.of(
                "userID", user.getId(),
                "roles", user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );

        String accessToken = jwtService.generateToken(user.getEmail(), claims, JWTServiceImpl.JwtType.ACCESS);
        String refreshToken = jwtService.generateToken(user.getEmail(), claims, JWTServiceImpl.JwtType.REFRESH);

        RefreshTokenEntity refreshTokenEntity =
                RefreshTokenEntity.builder()
                        .token(refreshToken)
                        .user(user)
                        .expiresAt(jwtService.getExpirationInstant(JWTServiceImpl.JwtType.REFRESH))
                        .revoked(false)
                        .build();

        refreshTokenRepository.save(refreshTokenEntity);
        user.getRefreshTokens().add(refreshTokenEntity);

        return Pair.of(accessToken, refreshToken);
    }
}
