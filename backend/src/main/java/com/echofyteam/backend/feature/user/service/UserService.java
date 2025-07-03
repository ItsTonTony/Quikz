package com.echofyteam.backend.feature.user.service;

import com.echofyteam.backend.feature.user.dto.request.CreateUserRequest;
import com.echofyteam.backend.feature.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserResponse getUserById(UUID userID);
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse createUser(CreateUserRequest createUserRequest);
    void deleteUser(UUID userID);

    UserResponse updateUsername(UUID userID, String username);
}
