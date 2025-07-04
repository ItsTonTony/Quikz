package com.echofyteam.backend.feature.auth.repository;

import com.echofyteam.backend.feature.auth.entity.RefreshTokenEntity;
import com.echofyteam.backend.feature.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByUser(UserEntity user);

    @Modifying
    @Transactional
    int deleteByExpiresAtBefore(Instant now);

    @Modifying
    @Transactional
    int deleteByRevokedTrue();
}
