package com.echofyteam.backend.feature.user.service;

import com.echofyteam.backend.feature.user.dto.request.CreateUserRequest;
import com.echofyteam.backend.feature.user.dto.response.UserResponse;
import com.echofyteam.backend.feature.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserResponse getUserByID(UUID userID);
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse createUser(CreateUserRequest createUserRequest);
    void deleteUser(UUID userID);

    UserResponse updateUsername(UUID userID, String username);
    Optional<UserEntity> getCurrentUser();
}
