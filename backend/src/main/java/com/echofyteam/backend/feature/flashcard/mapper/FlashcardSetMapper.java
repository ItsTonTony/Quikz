package com.echofyteam.backend.feature.flashcard.mapper;

import com.echofyteam.backend.feature.flashcard.dto.response.FlashcardSetResponse;
import com.echofyteam.backend.feature.flashcard.entity.FlashcardSetEntity;
import org.springframework.stereotype.Component;

@Component
public class FlashcardSetMapper {
    public FlashcardSetResponse toFlashcardSetResponse(FlashcardSetEntity flashcardSetEntity) {
        return FlashcardSetResponse.builder()
                .id(flashcardSetEntity.getId())
                .title(flashcardSetEntity.getTitle())
                .previewUrl(flashcardSetEntity.getPreviewUrl())
                .cardCount(flashcardSetEntity.getFlashcards().size())
                .authorName(flashcardSetEntity.getAuthor().getUsername())
                .publicFlag(flashcardSetEntity.isPublicFlag())
                .build();
    }
}
