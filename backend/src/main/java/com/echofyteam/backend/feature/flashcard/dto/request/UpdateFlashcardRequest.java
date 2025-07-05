package com.echofyteam.backend.feature.flashcard.dto.request;

import com.echofyteam.backend.feature.flashcard.entity.FlashcardDifficultyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

/**
 * DTO for partially updating a flashcard.
 * <p>
 * All fields are optional for partial update (PATCH).
 * </p>
 *
 * @param word            optional new word or phrase of the flashcard
 * @param description     optional new explanation or meaning of the word
 * @param translation     optional new translation of the word
 * @param difficultyLevel optional new difficulty level of the flashcard
 * @param useExamples     optional new list of example sentences or usages of the word
 */
@Builder
@Schema(name = "PatchFlashcardRequest", description = "Request payload for partially updating a flashcard")
public record UpdateFlashcardRequest(
        @Schema(description = "Word or phrase", example = "apple", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Optional<String> word,

        @Schema(description = "Explanation or description of the word", example = "A fruit that grows on trees", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Optional<String> description,

        @Schema(description = "Translation of the word", example = "яблоко", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Optional<String> translation,

        @Schema(description = "Difficulty level of the flashcard", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Optional<FlashcardDifficultyLevel> difficultyLevel,

        @Schema(description = "List of example usages", example = "[\"I ate an apple.\", \"Apple pie is delicious.\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Optional<List<String>> useExamples
) {}
