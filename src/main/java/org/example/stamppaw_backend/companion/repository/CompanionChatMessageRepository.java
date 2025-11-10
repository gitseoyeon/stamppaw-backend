package org.example.stamppaw_backend.companion.repository;

import org.example.stamppaw_backend.companion.entity.CompanionChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanionChatMessageRepository extends JpaRepository<CompanionChatMessage, Long> {
    Page<CompanionChatMessage> findByChatRoomIdOrderByRegisteredAtDesc(Long chatRoomId, Pageable pageable);
}

