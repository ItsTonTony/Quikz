package com.echofyteam.backend.feature.flashcard.mapper;

import com.echofyteam.backend.feature.flashcard.dto.response.UserFlashcardProgressResponse;
import com.echofyteam.backend.feature.flashcard.entity.UserFlashcardProgressEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFlashcardProgressMapper {
    private final FlashcardMapper flashcardMapper;

    public UserFlashcardProgressResponse toUserFlashcardProgressResponse(UserFlashcardProgressEntity userFlashcardProgressEntity) {
        return UserFlashcardProgressResponse.builder()
                .flashcardResponse(flashcardMapper.toFlashcardResponse(userFlashcardProgressEntity.getFlashcard()))
                .learningLevel(userFlashcardProgressEntity.getLearningLevel())
                .repetitionCount(userFlashcardProgressEntity.getRepetitionCount())
                .lastReviewedAt(userFlashcardProgressEntity.getLastReviewedAt())
                .build();
    }
}
