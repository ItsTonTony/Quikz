package com.echofyteam.backend.feature.auth.service;

import com.echofyteam.backend.feature.auth.dto.request.SignInRequest;
import com.echofyteam.backend.feature.auth.dto.request.SignUpRequest;
import com.echofyteam.backend.feature.auth.dto.response.RefreshTokenResponse;
import com.echofyteam.backend.feature.auth.dto.response.SignInResponse;
import com.echofyteam.backend.feature.auth.dto.response.SignUpResponse;

public interface AuthService {
    SignInResponse signIn(SignInRequest signInRequest);
    SignUpResponse signUp(SignUpRequest signUpRequest);
    void signOut(String refreshToken);
    RefreshTokenResponse refreshToken(String refreshToken);
}
