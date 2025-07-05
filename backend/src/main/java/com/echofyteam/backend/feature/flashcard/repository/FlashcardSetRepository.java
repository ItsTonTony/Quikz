package com.echofyteam.backend.feature.flashcard.repository;

import com.echofyteam.backend.feature.flashcard.entity.FlashcardSetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FlashcardSetRepository extends JpaRepository<FlashcardSetEntity, UUID> {
    Page<FlashcardSetEntity> findAllByPublicFlagTrue(Pageable pageable);
    Page<FlashcardSetEntity> findAllByAuthorId(UUID authorId, Pageable pageable);
}
