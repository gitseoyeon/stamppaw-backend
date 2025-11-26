package org.example.stamppaw_backend.parttime.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.companion.entity.ApplyStatus;
import org.example.stamppaw_backend.parttime.dto.response.PartTimeUserApplyResponse;
import org.example.stamppaw_backend.parttime.entity.PartTime;
import org.example.stamppaw_backend.parttime.entity.PartTimeApply;
import org.example.stamppaw_backend.parttime.repository.PartTimeApplyRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartTimeApplyService {

    private final PartTimeApplyRepository partTimeApplyRepository;
    private final UserService userService;

    public void savePartTimeApply(User user, PartTime partTime) {
        isAlreadyApply(user, partTime.getApplies());

        PartTimeApply apply = PartTimeApply.builder()
            .partTime(partTime)
            .applicant(user)
            .build();

        partTimeApplyRepository.save(apply);
    }

    @Transactional(readOnly = true)
    public Page<PartTimeApply> getPartTimeApply(PartTime partTime, Pageable pageable) {
        return partTimeApplyRepository.findAllByPartTime(partTime, pageable);
    }

    public void changeApplyStatus(Long applyId, ApplyStatus status) {
        PartTimeApply apply = partTimeApplyRepository.findById(applyId)
            .orElseThrow(() -> new StampPawException(ErrorCode.PARTTIME_APPLY_NOT_FOUND));

        if (apply.getStatus().equals(status)) {
            throw new StampPawException(ErrorCode.ALREADY_CHANGE_STATUS);
        }

        apply.changeStatus(status);
    }

    @Transactional(readOnly = true)
    public Page<PartTimeUserApplyResponse> getUserApply(Pageable pageable, Long userId) {
        User user = userService.getUserOrException(userId);
        Page<PartTimeApply> applies = partTimeApplyRepository.findPartTimeApplyByApplicant(user, pageable);

        return applies.map(PartTimeUserApplyResponse::from);
    }


    private void isAlreadyApply(User user, List<PartTimeApply> applies) {
        for (PartTimeApply apply : applies) {
            if (apply.getApplicant().equals(user)) {
                throw new StampPawException(ErrorCode.ALREADY_APPLICANT);
            }
        }
    }
}
