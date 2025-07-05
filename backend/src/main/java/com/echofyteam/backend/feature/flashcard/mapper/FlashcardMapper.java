package com.echofyteam.backend.feature.flashcard.mapper;

import com.echofyteam.backend.feature.flashcard.dto.response.FlashcardResponse;
import com.echofyteam.backend.feature.flashcard.entity.FlashcardEntity;
import org.springframework.stereotype.Component;

@Component
public class FlashcardMapper {
    public FlashcardResponse toFlashcardResponse(FlashcardEntity flashcardEntity) {
        return FlashcardResponse.builder()
                .id(flashcardEntity.getId())
                .word(flashcardEntity.getWord())
                .description(flashcardEntity.getDescription())
                .translation(flashcardEntity.getTranslation())
                .useExamples(flashcardEntity.getUseExamples())
                .build();
    }
}
