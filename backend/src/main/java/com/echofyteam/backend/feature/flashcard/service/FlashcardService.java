package com.echofyteam.backend.feature.flashcard.service;

import com.echofyteam.backend.feature.flashcard.dto.request.CreateFlashcardRequest;
import com.echofyteam.backend.feature.flashcard.dto.request.UpdateFlashcardProgressRequest;
import com.echofyteam.backend.feature.flashcard.dto.request.UpdateFlashcardRequest;
import com.echofyteam.backend.feature.flashcard.dto.response.FlashcardResponse;
import com.echofyteam.backend.feature.flashcard.dto.response.UserFlashcardProgressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface FlashcardService {
    FlashcardResponse getFlashcardByID(UUID flashcardSetID, UUID flashcardID);
    FlashcardResponse getRandomFlashcard(UUID flashcardSetID);
    Page<FlashcardResponse> getAllFlashcards(UUID flashcardSetID, Pageable pageable);
    FlashcardResponse createFlashcard(UUID flashcardSetID, CreateFlashcardRequest createFlashcardRequest);
    List<FlashcardResponse> batchFlashcards(UUID flashcardSetID, List<CreateFlashcardRequest> createFlashcardRequests);
    FlashcardResponse updateFlashcard(UUID flashcardSetID, UUID flashcardID, UpdateFlashcardRequest updateFlashcardRequest);
    UserFlashcardProgressResponse updateFlashcardProgress(UUID flashcardSetID, UUID flashcardID, UpdateFlashcardProgressRequest updateFlashcardProgressRequest);
    void deleteFlashcard(UUID flashcardSetID, UUID flashcardID);
}
