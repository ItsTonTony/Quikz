package com.echofyteam.backend.feature.auth.service.impl;

import com.echofyteam.backend.feature.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenCleanupService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void removeExpiredTokens() {
        log.info("Starting cleanup of expired refresh tokens");
        int deletedCount = refreshTokenRepository.deleteByExpiresAtBefore(Instant.now());
        log.info("Deleted {} expired refresh tokens", deletedCount);
    }

    @Scheduled(cron = "0 30 3 * * *")
    @Transactional
    public void removeRevokedTokens() {
        log.info("Starting cleanup of revoked refresh tokens");
        int deletedCount = refreshTokenRepository.deleteByRevokedTrue();
        log.info("Deleted {} revoked refresh tokens", deletedCount);
    }
}