package com.echofyteam.backend.feature.flashcard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

/**
 * Response DTO representing a flashcard.
 *
 * @param id            unique identifier of the flashcard
 * @param word          the word or phrase of the flashcard
 * @param description   explanation or meaning of the word
 * @param translation   translation of the word
 * @param useExamples   list of example sentences or usages of the word
 */
@Builder
@Schema(name = "FlashcardResponse", description = "Response payload representing a flashcard")
public record FlashcardResponse(
        @Schema(description = "Unique identifier of the flashcard", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "Word or phrase of the flashcard", example = "apple")
        String word,

        @Schema(description = "Explanation or meaning of the word", example = "A fruit that grows on trees")
        String description,

        @Schema(description = "Translation of the word", example = "яблоко")
        String translation,

        @Schema(description = "List of example sentences or usages", example = "[\"I ate an apple.\", \"Apple pie is delicious.\"]")
        List<String> useExamples
) {
}