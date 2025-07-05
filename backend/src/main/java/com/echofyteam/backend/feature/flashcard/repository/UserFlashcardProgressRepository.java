package com.echofyteam.backend.feature.flashcard.repository;

import com.echofyteam.backend.feature.flashcard.entity.UserFlashcardProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserFlashcardProgressRepository extends JpaRepository<UserFlashcardProgressEntity, UUID> {
    Optional<UserFlashcardProgressEntity> findByUserIdAndFlashcardId(UUID userID, UUID flashcardID);
}
