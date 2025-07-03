package com.echofyteam.backend.feature.user.mapper;

import com.echofyteam.backend.feature.user.dto.response.UserResponse;
import com.echofyteam.backend.feature.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .lastLogin(userEntity.getLastLoginAt())
                .build();
    }
}
