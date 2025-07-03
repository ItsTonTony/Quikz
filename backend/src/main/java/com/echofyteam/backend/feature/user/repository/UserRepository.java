package com.echofyteam.backend.feature.user.repository;

import com.echofyteam.backend.feature.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);
    boolean existsByUsernameAndIdNot(String username, UUID id);
}
