package org.example.stamppaw_backend.companion.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.companion.dto.response.CompanionApplyResponse;
import org.example.stamppaw_backend.companion.dto.response.CompanionResponse;
import org.example.stamppaw_backend.companion.entity.ApplyStatus;
import org.example.stamppaw_backend.companion.entity.Companion;
import org.example.stamppaw_backend.companion.entity.CompanionApply;
import org.example.stamppaw_backend.companion.repository.CompanionApplyRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanionApplyService {
    private final CompanionApplyRepository companionApplyRepository;

    public void saveCompanionApply(User user, Companion companion) {
        isAlreadyApply(user, companion.getApplies());
        CompanionApply companionApply = CompanionApply.builder()
                .companion(companion)
                .applicant(user)
                .build();

        companionApplyRepository.save(companionApply);
    }

    @Transactional(readOnly = true)
    public Page<CompanionApply> getCompanionApply(Companion companion, Pageable pageable) {
        return companionApplyRepository.findAllByCompanion(companion, pageable);
    }

    public void changeApplyStatus(Long applyId, ApplyStatus status) {
        CompanionApply companionApply = companionApplyRepository.findById(applyId)
                .orElseThrow(() -> new StampPawException(ErrorCode.COMPANION_APPLY_NOT_FOUND));
        if(companionApply.getStatus().equals(status)) {
            throw new StampPawException(ErrorCode.ALREADY_CHANGE_STATUS);
        }
        companionApply.changeStatus(status);
    }

    private void isAlreadyApply(User user, List<CompanionApply> applies) {
        for(CompanionApply apply : applies) {
            if(apply.getApplicant().equals(user)) {
                throw new StampPawException(ErrorCode.ALREADY_APPLICANT);
            }
        }
    }
}
