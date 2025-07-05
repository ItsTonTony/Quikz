package com.echofyteam.backend.feature.flashcard.service.impl;

import com.echofyteam.backend.exception.impl.BusinessException;
import com.echofyteam.backend.exception.impl.BusinessExceptionReason;
import com.echofyteam.backend.feature.flashcard.dto.request.CreateFlashcardSetRequest;
import com.echofyteam.backend.feature.flashcard.dto.request.UpdateFlashcardSetRequest;
import com.echofyteam.backend.feature.flashcard.dto.response.FlashcardSetResponse;
import com.echofyteam.backend.feature.flashcard.entity.FlashcardSetEntity;
import com.echofyteam.backend.feature.flashcard.mapper.FlashcardSetMapper;
import com.echofyteam.backend.feature.flashcard.repository.FlashcardSetRepository;
import com.echofyteam.backend.feature.flashcard.service.FlashcardSetService;
import com.echofyteam.backend.feature.user.entity.UserEntity;
import com.echofyteam.backend.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlashcardSetServiceImpl implements FlashcardSetService {
    private final FlashcardSetRepository flashcardSetRepository;
    private final FlashcardSetMapper flashcardSetMapper;
    private final UserService userService;

    @Override
    public Page<FlashcardSetResponse> getAllPublicFlashcardSets(Pageable pageable) {
        Page<FlashcardSetEntity> flashcardSets = flashcardSetRepository.findAllByPublicFlagTrue(pageable);

        return flashcardSets.map(flashcardSetMapper::toFlashcardSetResponse);
    }

    @Override
    public Page<FlashcardSetResponse> getAllPersonalFlashcardSets(Pageable pageable) {
        UserEntity currentUser = userService.getCurrentUser()
                .orElseThrow(() -> {
                    log.warn("User not authenticated - forbidden to create flashcard set");
                    return new BusinessException(BusinessExceptionReason.FORBIDDEN);
                });

        Page<FlashcardSetEntity> flashcardSets = flashcardSetRepository.findAllByAuthorId(currentUser.getId(), pageable);

        return flashcardSets.map(flashcardSetMapper::toFlashcardSetResponse);
    }

    @Override
    public FlashcardSetResponse getFlashcardSetByID(UUID flashcardSetID) {
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
            throw  new BusinessException(BusinessExceptionReason.FORBIDDEN);
        }

        return flashcardSetMapper.toFlashcardSetResponse(flashcardSet);
    }

    @Override
    @Transactional
    public FlashcardSetResponse createFlashcardSet(CreateFlashcardSetRequest createFlashcardSetRequest) {
        UserEntity currentUser = userService.getCurrentUser()
                .orElseThrow(() -> {
                    log.warn("User not authenticated - forbidden to create flashcard set");
                    return new BusinessException(BusinessExceptionReason.FORBIDDEN);
                });

        log.info("User with id: {} is creating flashcard set titled: '{}'", currentUser.getId(), createFlashcardSetRequest.title());

        String previewUrl = createFlashcardSetRequest.previewUrl();
        if (previewUrl != null && previewUrl.isBlank()) {
            previewUrl = null;
        }

        FlashcardSetEntity flashcardSet = FlashcardSetEntity.builder()
                .title(createFlashcardSetRequest.title())
                .previewUrl(previewUrl)
                .author(currentUser)
                .flashcards(new ArrayList<>())
                .publicFlag(createFlashcardSetRequest.publicFlag())
                .build();

        FlashcardSetEntity savedFlashcardSet = flashcardSetRepository.save(flashcardSet);

        log.info("Flashcard set with id: {} and title: '{}' created successfully by user id: {}", savedFlashcardSet.getId(), savedFlashcardSet.getTitle(), currentUser.getId());

        return flashcardSetMapper.toFlashcardSetResponse(savedFlashcardSet);
    }

    @Override
    @Transactional
    public FlashcardSetResponse updateFlashcardSet(UUID flashcardSetID, UpdateFlashcardSetRequest updateFlashcardSetRequest) {
        log.info("Attempting to update flashcard set with ID: {}", flashcardSetID);

        UserEntity currentUser = userService.getCurrentUser()
                .orElseThrow(() -> {
                    log.warn("User not authenticated - forbidden to create flashcard set");
                    return new BusinessException(BusinessExceptionReason.FORBIDDEN);
                });

        FlashcardSetEntity flashcardSet = flashcardSetRepository.findById(flashcardSetID)
                .orElseThrow(() -> {
                    log.warn("Flashcard set with ID: {} not found for update operation.", flashcardSetID);
                    return new BusinessException(BusinessExceptionReason.FLASHCARD_SET_NOT_FOUND);
                });

        if (!flashcardSet.getAuthor().getId().equals(currentUser.getId())) {
            throw  new BusinessException(BusinessExceptionReason.FORBIDDEN);
        }

        updateFlashcardSetRequest.title()
                .filter(title -> !title.isBlank())
                .ifPresent(flashcardSet::setTitle);

        updateFlashcardSetRequest.previewUrl()
                .map(url -> url.isBlank() ? null : url)
                .ifPresent(flashcardSet::setPreviewUrl);

        updateFlashcardSetRequest.publicFlag()
                .ifPresent(flashcardSet::setPublicFlag);

        FlashcardSetEntity updatedFlashcardSet = flashcardSetRepository.save(flashcardSet);

        log.info("Flashcard set with ID: {} updated successfully.", flashcardSetID);
        return flashcardSetMapper.toFlashcardSetResponse(updatedFlashcardSet);
    }

    @Override
    @Transactional
    public void deleteFlashcardSet(UUID flashcardSetID) {
        log.info("Attempting to delete flashcard set with id: {}", flashcardSetID);

        UserEntity currentUser = userService.getCurrentUser()
                .orElseThrow(() -> {
                    log.warn("User not authenticated - forbidden to create flashcard set");
                    return new BusinessException(BusinessExceptionReason.FORBIDDEN);
                });

        FlashcardSetEntity flashcardSet = flashcardSetRepository.findById(flashcardSetID)
                .orElseThrow(() -> {
                    log.warn("Flashcard set with id: {} not found for deletion", flashcardSetID);
                    return new BusinessException(BusinessExceptionReason.FLASHCARD_SET_NOT_FOUND);
                });

        if (!flashcardSet.getAuthor().getId().equals(currentUser.getId())) {
            throw  new BusinessException(BusinessExceptionReason.FORBIDDEN);
        }

        flashcardSetRepository.deleteById(flashcardSetID);
        log.info("Flashcard set with id: {} deleted successfully", flashcardSetID);
    }
}
