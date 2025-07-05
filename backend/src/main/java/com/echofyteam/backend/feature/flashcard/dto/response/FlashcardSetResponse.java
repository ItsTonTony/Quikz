package com.echofyteam.backend.feature.flashcard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

/**
 * Response DTO representing a flashcard set.
 *
 * @param id         unique identifier of the flashcard set
 * @param title      title of the flashcard set
 * @param previewUrl URL of the preview image for the flashcard set
 * @param cardCount  number of flashcards contained in the set
 * @param authorName name of flashcard set author
 * @param publicFlag publicity of flashcard set
 */
@Builder
@Schema(name = "FlashcardSetResponse", description = "Response payload representing a flashcard set")
public record FlashcardSetResponse(
        @Schema(description = "Unique identifier of the flashcard set", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "Title of the flashcard set", example = "Basic English Vocabulary")
        String title,

        @Schema(description = "URL of the preview image", example = "https://example.com/images/preview.jpg")
        String previewUrl,

        @Schema(description = "Number of flashcards in the set", example = "42")
        int cardCount,

        @Schema(description = "Name of flashcard set author", example = "testuser")
        String authorName,

        @Schema(description = "Publicity of flashcard set", example = "true")
        boolean publicFlag
) {
}