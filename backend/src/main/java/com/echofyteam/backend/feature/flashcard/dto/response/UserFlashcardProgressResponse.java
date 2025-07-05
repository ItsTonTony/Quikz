package com.echofyteam.backend.feature.flashcard.dto.response;

import com.echofyteam.backend.feature.flashcard.entity.FlashcardLearningLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;

/**
 * DTO representing a user's progress on a specific flashcard.
 *
 * @param flashcardResponse the flashcard details
 * @param learningLevel     the current learning level of the flashcard for the user
 * @param repetitionCount   the number of times the flashcard has been reviewed
 * @param lastReviewedAt    the timestamp of the most recent review
 */
@Builder
@Schema(name = "UserFlashcardProgressResponse", description = "Response object representing user's learning progress on a flashcard")
public record UserFlashcardProgressResponse(
        @Schema(description = "Flashcard details", requiredMode = Schema.RequiredMode.REQUIRED)
        FlashcardResponse flashcardResponse,

        @Schema(description = "User's current learning level for this flashcard", example = "LEARNING", requiredMode = Schema.RequiredMode.REQUIRED)
        FlashcardLearningLevel learningLevel,

        @Schema(description = "Number of times this flashcard has been reviewed", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        int repetitionCount,

        @Schema(description = "Timestamp of the last time this flashcard was reviewed", example = "2025-07-05T10:15:30Z", requiredMode = Schema.RequiredMode.REQUIRED)
        Instant lastReviewedAt
) {}