package org.example.stamppaw_backend.companion.repository;

import org.example.stamppaw_backend.companion.entity.Companion;
import org.example.stamppaw_backend.companion.entity.CompanionApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanionApplyRepository extends JpaRepository<CompanionApply, Long> {
    Page<CompanionApply> findAllByCompanion(Companion companion, Pageable pageable);
}
