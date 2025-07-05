package com.echofyteam.backend.feature.flashcard.repository;

import com.echofyteam.backend.feature.flashcard.entity.FlashcardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlashcardRepository extends JpaRepository<FlashcardEntity, UUID> {
    Optional<FlashcardEntity> findByIdAndSetId(UUID flashcardID, UUID setId);

    Page<FlashcardEntity> findAllBySetId(UUID setId, Pageable pageable);

    @Query(value = "SELECT * FROM flashcards WHERE set_id = :setId ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<FlashcardEntity> findRandomBySetId(@Param("setId") UUID flashcardSetID);

    boolean existsByWordAndSetId(String word, UUID setId);

    @Query("SELECT f.word FROM FlashcardEntity f WHERE f.set.id = :setId")
    List<String> findWordsBySetId(@Param("setId") UUID setId);

    void deleteByIdAndSetId(UUID id, UUID setId);
}
