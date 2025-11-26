package org.example.stamppaw_backend.parttime.repository;

import org.example.stamppaw_backend.parttime.entity.PartTime;
import org.example.stamppaw_backend.parttime.entity.PartTimeApply;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.data
    .domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartTimeApplyRepository extends JpaRepository<PartTimeApply, Long> {

    Page<PartTimeApply> findAllByPartTime(PartTime partTime, Pageable pageable);

    Page<PartTimeApply> findPartTimeApplyByApplicant(User user, Pageable pageable);

    Page<PartTimeApply> findByApplicant(User user, Pageable pageable);

}
