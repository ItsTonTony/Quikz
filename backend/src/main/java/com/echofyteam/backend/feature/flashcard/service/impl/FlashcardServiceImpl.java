package com.echofyteam.backend.feature.flashcard.service.impl;

import com.echofyteam.backend.exception.impl.BusinessException;
import com.echofyteam.backend.exception.impl.BusinessExceptionReason;
import com.echofyteam.backend.feature.flashcard.dto.request.CreateFlashcardRequest;
import com.echofyteam.backend.feature.flashcard.dto.request.UpdateFlashcardProgressRequest;
import com.echofyteam.backend.feature.flashcard.dto.request.UpdateFlashcardRequest;
import com.echofyteam.backend.feature.flashcard.dto.response.FlashcardResponse;
import com.echofyteam.backend.feature.flashcard.dto.response.UserFlashcardProgressResponse;
import com.echofyteam.backend.feature.flashcard.entity.FlashcardEntity;
import com.echofyteam.backend.feature.flashcard.entity.FlashcardLearningLevel;
import com.echofyteam.backend.feature.flashcard.entity.FlashcardSetEntity;
import com.echofyteam.backend.feature.flashcard.entity.UserFlashcardProgressEntity;
import com.echofyteam.backend.feature.flashcard.mapper.FlashcardMapper;
import com.echofyteam.backend.feature.flashcard.mapper.UserFlashcardProgressMapper;
import com.echofyteam.backend.feature.flashcard.repository.FlashcardRepository;
import com.echofyteam.backend.feature.flashcard.repository.FlashcardSetRepository;
import com.echofyteam.backend.feature.flashcard.repository.UserFlashcardProgressRepository;
import com.echofyteam.backend.feature.flashcard.service.FlashcardService;
import com.echofyteam.backend.feature.user.entity.UserEntity;
import com.echofyteam.backend.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService {
    private final FlashcardRepository flashcardRepository;
    private final FlashcardSetRepository flashcardSetRepository;
    private final UserFlashcardProgressRepository userFlashcardProgressRepository;
    private final FlashcardMapper flashcardMapper;
    private final UserFlashcardProgressMapper userFlashcardProgressMapper;
    private final UserService userService;

    @Override
    @Transactional
    public FlashcardResponse getFlashcardByID(UUID flashcardSetID, UUID flashcardID) {
        log.info("Getting flashcard with id: {} and set id: {}", flashcardID, flashcardSetID);

        UserEntity currentUser = userService.getCurrentUser()
                .orElseThrow(() -> {
                    log.warn("User not authenticated - forbidden to create flashcard set");
                    return new BusinessException(BusinessExceptionReason.FORBIDDEN);
                });

        FlashcardEntity flashcard = flashcardRepository.findByIdAndSetId(flashcardID, flashcardSetID)
                .orElseThrow(() -> {
                    log.warn("Flashcard with id: {} not found", flashcardID);
                    return new BusinessException(BusinessExceptionReason.FLASHCARD_NOT_FOUND);
                });

        FlashcardSetEntity flashcardSet = flashcard.getSet();

        if (!flashcardSet.isPublicFlag() && !flashcardSet.getAuthor().getId().equals(currentUser.getId())) {
            log.warn("User not authenticated - forbidden to create flashcard set");
            throw new BusinessException(BusinessExceptionReason.FORBIDDEN);
        }

        log.info("Flashcard with id: {} and word: {} retrieved successfully", flashcardID, flashcard.getWord());
        return flashcardMapper.toFlashcardResponse(flashcard);
    }

    @Override
    @Transactional
    public FlashcardResponse getRandomFlashcard(UUID flashcardSetID) {
        log.info("Getting random flashcard from set with id: {}", flashcardSetID);

        UserEntity currentUser = userService.getCurrentUser()
                .orElseThrow(() -> {
                    log.warn("User not authenticated - forbidden to create flashcard set");
                    return new BusinessException(BusinessExceptionReason.FORBIDDEN);
                });

        FlashcardEntity flashcard = flashcardRepository.findRandomBySetId(flashcardSetID)
                .orElseThrow(() -> {
                    log.warn("Flashcard set with id: {} not found or empty", flashcardSetID);
                    return new BusinessException(BusinessExceptionReason.FLASHCARD_NOT_FOUND);
                });

        FlashcardSetEntity flashcardSet = flashcard.getSet();

        if (!flashcardSet.isPublicFlag() && !flashcardSet.getAuthor().getId().equals(currentUser.getId())) {
            log.warn("User not authenticated - forbidden to create flashcard set");
            throw new BusinessException(BusinessExceptionReason.FORBIDDEN);
        }

        log.info("Random flashcard with id: {} and word: {} retrieved successfully", flashcard.getId(), flashcard.getWord());
        return flashcardMapper.toFlashcardResponse(flashcard);
    }

    @Override
    @Transactional
    public Page<FlashcardResponse> getAllFlashcards(UUID flashcardSetID, Pageable pageable) {
        log.info("Getting flashcards for flashcard set with id: {}", flashcardSetID);

        UserEntity currentUser = userService.getCurrentUser()
                .orElseThrow(() -> {
                    log.warn("User not authenticated - forbidden to create flashcard set");
                    return new BusinessException(BusinessExceptionReason.FORBIDDEN);
                });

        FlashcardSetEntity flashcardSet = flashcardSetRepository.findById(flashcardSetID)
                .orElseThrow(() -> {
                    log.warn("Flashcard set with ID: {} not found.", flashcardSetID);
                    return new BusinessException(BusinessExceptionReason.FLASHCARD_SET_NOT_FOUND);
                });

        if (!flashcardSet.isPublicFlag() && !flashcardSet.getAuthor().getId().equals(currentUser.getId())) {
            log.warn("User not authenticated - forbidden to create flashcard set");
            throw new BusinessException(BusinessExceptionReason.FORBIDDEN);
        }

        Page<FlashcardEntity> flashcards = flashcardRepository.findAllBySetId(flashcardSetID, pageable);

        log.info("Retrieved {} flashcards for flashcard set with id: {}", flashcards.getTotalElements(), flashcardSetID);
        return flashcards.map(flashcardMapper::toFlashcardResponse);
    }

    @Override
    @Transactional
    public FlashcardResponse createFlashcard(UUID flashcardSetID, CreateFlashcardRequest createFlashcardRequest) {
        log.info("Creating flashcard with word: {} in flashcard set with id: {}", createFlashcardRequest.word(), flashcardSetID);

        UserEntity currentUser = userService.getCurrentUser()
                .orElseThrow(() -> {
                    log.warn("User not authenticated - forbidden to create flashcard set");
                    return new BusinessException(BusinessExceptionReason.FORBIDDEN);
                });

        FlashcardSetEntity flashcardSet = flashcardSetRepository.findById(flashcardSetID)
                .orElseThrow(() -> {
                    log.warn("Flashcard set with id: {} not found", flashcardSetID);
                    return new BusinessException(BusinessExceptionReason.FLASHCARD_SET_NOT_FOUND);
                });

        if (!flashcardSet.getAuthor().getId().equals(currentUser.getId())) {
            log.warn("User not authenticated - forbidden to create flashcard set");
            throw new BusinessException(BusinessExceptionReason.FORBIDDEN);
        }

        if (flashcardRepository.existsByWordAndSetId(createFlashcardRequest.word(), flashcardSetID)) {
            log.warn("Flashcard with word {} exists in flashcard set with id: {}", createFlashcardRequest.word(), flashcardSetID);
            throw new BusinessException(BusinessExceptionReason.FLASHCARD_WITH_THIS_WORD_IS_ALREADY_EXISTS);
        }

        List<String> useExamples = createFlashcardRequest.useExamples();
        if (useExamples == null) {
            useExamples = Collections.emptyList();
        } else {
            useExamples = useExamples.stream()
                    .filter(example -> example != null && !example.isBlank())
                    .toList();
        }

        FlashcardEntity flashcard = FlashcardEntity.builder()
                .set(flashcardSet)
                .word(createFlashcardRequest.word())
                .description(createFlashcardRequest.description())
                .translation(createFlashcardRequest.translation())
                .difficultyLevel(createFlashcardRequest.difficultyLevel())
                .useExamples(useExamples)
                .build();

        FlashcardEntity savedFlashcard = flashcardRepository.save(flashcard);
        flashcardSet.getFlashcards().add(savedFlashcard);

        log.info("Created flashcard with id: {} and word: {}", savedFlashcard.getId(), savedFlashcard.getWord());
        return flashcardMapper.toFlashcardResponse(savedFlashcard);
    }

    @Override
    @Transactional
    public List<FlashcardResponse> batchFlashcards(UUID flashcardSetID, List<CreateFlashcardRequest> createFlashcardRequests) {
        log.info("Batch creating flashcards in flashcard set with id: {}", flashcardSetID);

        UserEntity currentUser = userService.getCurrentUser()
                .orElseThrow(() -> {
                    log.warn("User not authenticated - forbidden to create flashcard set");
                    return new BusinessException(BusinessExceptionReason.FORBIDDEN);
                });

        FlashcardSetEntity flashcardSet = flashcardSetRepository.findById(flashcardSetID)
                .orElseThrow(() -> {
                    log.warn("Flashcard set with id: {} not found", flashcardSetID);
                    return new BusinessException(BusinessExceptionReason.FLASHCARD_SET_NOT_FOUND);
                });

        if (!flashcardSet.getAuthor().getId().equals(currentUser.getId())) {
            log.warn("User not authenticated - forbidden to create flashcard set");
            throw new BusinessException(BusinessExceptionReason.FORBIDDEN);
        }

        Map<String, CreateFlashcardRequest> uniqueRequests = createFlashcardRequests.stream()
                .collect(Collectors.toMap(
                        CreateFlashcardRequest::word,
                        cr -> cr,
                        (existing, replacement) -> existing
                ));

        List<String> existingWords = flashcardRepository.findWordsBySetId(flashcardSetID);
        Set<String> existingWordsSet = new HashSet<>(existingWords);

        List<CreateFlashcardRequest> filteredRequests = uniqueRequests.values().stream()
                .filter(cr -> !existingWordsSet.contains(cr.word()))
                .toList();

        if (filteredRequests.isEmpty()) {
            log.info("No new flashcards to create for flashcard set with id: {}", flashcardSetID);
            return Collections.emptyList();
        }

        List<FlashcardEntity> flashcardEntities = filteredRequests.stream()
                .map(createFlashcardRequest -> {
                    List<String> useExamples = createFlashcardRequest.useExamples();
                    if (useExamples == null) {
                        useExamples = Collections.emptyList();
                    } else {
                        useExamples = useExamples.stream()
                                .filter(example -> example != null && !example.isBlank())
                                .toList();
                    }

                    return FlashcardEntity.builder()
                            .set(flashcardSet)
                            .word(createFlashcardRequest.word())
                            .description(createFlashcardRequest.description())
                            .translation(createFlashcardRequest.translation())
                            .difficultyLevel(createFlashcardRequest.difficultyLevel())
                            .useExamples(useExamples)
                            .build();
                })
                .toList();

        List<FlashcardEntity> savedFlashcards = flashcardRepository.saveAll(flashcardEntities);
        flashcardSet.getFlashcards().addAll(savedFlashcards);

        log.info("Batch created {} flashcards in flashcard set with id: {}", savedFlashcards.size(), flashcardSetID);
        return savedFlashcards.stream()
                .map(flashcardMapper::toFlashcardResponse)
                .toList();
    }

    @Override
    @Transactional
    public FlashcardResponse updateFlashcard(UUID flashcardSetID, UUID flashcardID, UpdateFlashcardRequest updateFlashcardRequest) {
        log.info("Updating flashcard with id: {} in flashcard set with id: {}", flashcardID, flashcardSetID);

        UserEntity currentUser = userService.getCurrentUser()
                .orElseThrow(() -> {
                    log.warn("User not authenticated - forbidden to create flashcard set");
                    return new BusinessException(BusinessExceptionReason.FORBIDDEN);
                });

        FlashcardSetEntity flashcardSet = flashcardSetRepository.findById(flashcardSetID)
                .orElseThrow(() -> {
                    log.warn("Flashcard set with id: {} not found", flashcardSetID);
                    return new BusinessException(BusinessExceptionReason.FLASHCARD_SET_NOT_FOUND);
                });

        if (!flashcardSet.getAuthor().getId().equals(currentUser.getId())) {
            log.warn("User not authenticated - forbidden to create flashcard set");
            throw new BusinessException(BusinessExceptionReason.FORBIDDEN);
        }

        FlashcardEntity flashcard = flashcardRepository.findByIdAndSetId(flashcardID, flashcardSetID)
                .orElseThrow(() -> {
                    log.warn("Flashcard with id: {} not found", flashcardID);
                    return new BusinessException(BusinessExceptionReason.FLASHCARD_NOT_FOUND);
                });

        updateFlashcardRequest.word()
                .filter(word -> !word.isBlank())
                .ifPresent(flashcard::setWord);

        updateFlashcardRequest.description()
                .filter(desc -> !desc.isBlank())
                .ifPresent(flashcard::setDescription);

        updateFlashcardRequest.translation()
                .ifPresent(flashcard::setTranslation);

        updateFlashcardRequest.difficultyLevel()
                .ifPresent(flashcard::setDifficultyLevel);

        updateFlashcardRequest.useExamples()
                .map(list -> list.stream()
                        .filter(example -> example != null && !example.isBlank())
                        .toList())
                .ifPresent(flashcard::setUseExamples);

        FlashcardEntity updatedFlashcard = flashcardRepository.save(flashcard);

        log.info("Updated flashcard with id: {} successfully", flashcardID);
        return flashcardMapper.toFlashcardResponse(updatedFlashcard);
    }

    @Override
    @Transactional
    public UserFlashcardProgressResponse updateFlashcardProgress(UUID flashcardSetID, UUID flashcardID, UpdateFlashcardProgressRequest updateFlashcardProgressRequest) {
        log.info("Updating progress for flashcard with id: {} in flashcard set with id: {}", flashcardID, flashcardSetID);

        UserEntity currentUser = userService.getCurrentUser()
                .orElseThrow(() -> {
                    log.warn("User not authenticated - forbidden to create flashcard set");
                    return new BusinessException(BusinessExceptionReason.FORBIDDEN);
                });

        FlashcardSetEntity flashcardSet = flashcardSetRepository.findById(flashcardSetID)
                .orElseThrow(() -> {
                    log.warn("Flashcard set with id: {} not found", flashcardSetID);
                    return new BusinessException(BusinessExceptionReason.FLASHCARD_SET_NOT_FOUND);
                });

        if (!flashcardSet.isPublicFlag() && !flashcardSet.getAuthor().getId().equals(currentUser.getId())) {
            log.warn("User not authenticated - forbidden to create flashcard set");
            throw new BusinessException(BusinessExceptionReason.FORBIDDEN);
        }

        FlashcardEntity flashcard = flashcardRepository.findByIdAndSetId(flashcardID, flashcardSetID)
                .orElseThrow(() -> {
                    log.warn("Flashcard with id: {} not found", flashcardID);
                    return new BusinessException(BusinessExceptionReason.FLASHCARD_NOT_FOUND);
                });

        Optional<UserFlashcardProgressEntity> optionalProgress = userFlashcardProgressRepository
                .findByUserIdAndFlashcardId(currentUser.getId(), flashcardID);

        FlashcardLearningLevel learningLevel = updateFlashcardProgressRequest.isUnsure()
                ? FlashcardLearningLevel.UNSURE : FlashcardLearningLevel.CONFIDENT;

        UserFlashcardProgressEntity progress;
        if (optionalProgress.isPresent()) {
            progress = optionalProgress.get();
            log.info("Updating existing progress for user id: {} and flashcard id: {}", currentUser.getId(), flashcardID);
            progress.setLearningLevel(learningLevel);
            progress.setRepetitionCount(progress.getRepetitionCount() + 1);
        } else {
            progress = UserFlashcardProgressEntity.builder()
                    .user(currentUser)
                    .flashcard(flashcard)
                    .learningLevel(learningLevel)
                    .repetitionCount(1)
                    .build();
            log.info("Creating new progress for user id: {} and flashcard id: {}", currentUser.getId(), flashcardID);
        }

        progress.setLastReviewedAt(Instant.now());
        userFlashcardProgressRepository.save(progress);

        log.info("Progress update for flashcard with id: {} completed", flashcardID);

        return userFlashcardProgressMapper.toUserFlashcardProgressResponse(progress);
    }

    @Override
    @Transactional
    public void deleteFlashcard(UUID flashcardSetID, UUID flashcardID) {
        log.info("Deleting flashcard with id: {} from flashcard set with id: {}", flashcardID, flashcardSetID);

        UserEntity currentUser = userService.getCurrentUser()
                .orElseThrow(() -> {
                    log.warn("User not authenticated - forbidden to create flashcard set");
                    return new BusinessException(BusinessExceptionReason.FORBIDDEN);
                });

        FlashcardSetEntity flashcardSet = flashcardSetRepository.findById(flashcardSetID)
                .orElseThrow(() -> {
                    log.warn("Flashcard set with id: {} not found", flashcardSetID);
                    return new BusinessException(BusinessExceptionReason.FLASHCARD_SET_NOT_FOUND);
                });

        if (!flashcardSet.getAuthor().getId().equals(currentUser.getId())) {
            log.warn("User not authenticated - forbidden to create flashcard set");
            throw new BusinessException(BusinessExceptionReason.FORBIDDEN);
        }

        flashcardRepository.deleteByIdAndSetId(flashcardID, flashcardSetID);

        log.info("Deleted flashcard with id: {} from flashcard set with id: {}", flashcardID, flashcardSetID);
    }
}
