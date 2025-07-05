package com.echofyteam.backend.feature.flashcard.service;

import com.echofyteam.backend.feature.flashcard.dto.request.CreateFlashcardSetRequest;
import com.echofyteam.backend.feature.flashcard.dto.request.UpdateFlashcardSetRequest;
import com.echofyteam.backend.feature.flashcard.dto.response.FlashcardSetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FlashcardSetService {
    Page<FlashcardSetResponse> getAllPublicFlashcardSets(Pageable pageable);
    Page<FlashcardSetResponse> getAllPersonalFlashcardSets(Pageable pageable);
    FlashcardSetResponse getFlashcardSetByID(UUID flashcardSetID);
    FlashcardSetResponse createFlashcardSet(CreateFlashcardSetRequest createFlashcardSetRequest);
    FlashcardSetResponse updateFlashcardSet(UUID flashcardSetID, UpdateFlashcardSetRequest updateFlashcardSetRequest);
    void deleteFlashcardSet(UUID flashcardSetID);
}
