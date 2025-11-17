package org.example.stamppaw_backend.companion.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.companion.entity.CompanionReviewTag;
import org.example.stamppaw_backend.companion.repository.CompanionReviewTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanionReviewTagService {
    private final CompanionReviewTagRepository reviewTagRepository;

    @Transactional(readOnly = true)
    public List<CompanionReviewTag> getTags(List<Long> list) {
        List<CompanionReviewTag> companionReviewTags = new ArrayList<>();
        for(Long id : list) {
            companionReviewTags.add(getTagOrException(id));
        }

        return companionReviewTags;
    }

    @Transactional(readOnly = true)
    public List<CompanionReviewTag> getAllTags() {
        return reviewTagRepository.findAll();
    }

    public CompanionReviewTag getTagOrException(Long id) {
        return reviewTagRepository.findById(id)
                .orElseThrow(() -> new StampPawException(ErrorCode.TAG_NOT_FOUND));
    }
}
