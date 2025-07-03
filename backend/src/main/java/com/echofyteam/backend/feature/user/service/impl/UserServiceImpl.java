package com.echofyteam.backend.feature.user.service.impl;

import com.echofyteam.backend.exception.impl.BusinessException;
import com.echofyteam.backend.exception.impl.BusinessExceptionReason;
import com.echofyteam.backend.feature.user.dto.request.CreateUserRequest;
import com.echofyteam.backend.feature.user.dto.response.UserResponse;
import com.echofyteam.backend.feature.user.entity.UserEntity;
import com.echofyteam.backend.feature.user.mapper.UserMapper;
import com.echofyteam.backend.feature.user.repository.UserRepository;
import com.echofyteam.backend.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getUserById(UUID userID) {
        log.info("Getting warn by id: {}", userID);

        UserEntity userEntity = userRepository.findById(userID)
                .orElseThrow(() -> {
                    log.error("User with id: {} not found", userID);
                    return new BusinessException(BusinessExceptionReason.USER_NOT_FOUND);
                });

        log.info("User with id: {} found", userID);
        return userMapper.toUserResponse(userEntity);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.info("Getting page of users with page: {}, size: {}, sort: {}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<UserEntity> userEntities = userRepository.findAll(pageable);

        log.info("Found {} users on page {} of {}",
                userEntities.getNumberOfElements(), userEntities.getNumber(), userEntities.getTotalPages());
        return userEntities.map(userMapper::toUserResponse);
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        log.info("Creating an user with email: {}", createUserRequest.email());
        if (userRepository.existsByEmail(createUserRequest.email())) {
            log.error("User with email {} is already exist", createUserRequest.email());
            throw new BusinessException(BusinessExceptionReason.USER_ALREADY_EXISTS);
        }

        String username = createUserRequest.email().split("@")[0];

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .email(createUserRequest.email())
                .password(createUserRequest.password())
                .isDeleted(false)
                .isBlocked(false)
                .build();

        UserEntity savedUser = userRepository.save(userEntity);

        log.info("Created user with id: {}", savedUser.getId());
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userID) {
        log.info("Deleting user with id: {}", userID);
        UserEntity userEntity = userRepository.findById(userID)
                .orElseThrow(() -> {
                    log.warn("User with id: {} not found", userID);
                    return new BusinessException(BusinessExceptionReason.USER_NOT_FOUND);
                });

        userEntity.setDeleted(true);
        userRepository.save(userEntity);

        log.info("User with id: {} deleted", userID);
    }

    @Override
    @Transactional
    public UserResponse updateUsername(UUID userID, String username) {
        log.info("Changing username for user id: {} to '{}'", userID, username);

        UserEntity userEntity = userRepository.findById(userID)
                .orElseThrow(() -> {
                    log.warn("User with id: {} not found", userID);
                    return new BusinessException(BusinessExceptionReason.USER_NOT_FOUND);
                });

        if (userRepository.existsByUsernameAndIdNot(username, userID)) {
            log.warn("Username: {} is already taken by another user", username);
            throw new BusinessException(BusinessExceptionReason.USERNAME_IS_ALREADY_TAKEN);
        }

        userEntity.setUsername(username);
        UserEntity updatedUser = userRepository.save(userEntity);

        log.info("Username for user id: {} changed to '{}'", userID, username);
        return userMapper.toUserResponse(updatedUser);
    }
}
