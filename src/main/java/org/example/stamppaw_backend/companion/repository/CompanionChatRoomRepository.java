package org.example.stamppaw_backend.companion.repository;

import org.example.stamppaw_backend.companion.entity.CompanionChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanionChatRoomRepository extends JpaRepository<CompanionChatRoom, Long> {
    @Query(value = """
    SELECT DISTINCT r.*
    FROM companion_chat_rooms r
    LEFT JOIN companion_chat_room_participants p ON r.id = p.chat_room_id
    WHERE p.user_id = :userId OR r.creator_id = :userId
""", nativeQuery = true)
    List<CompanionChatRoom> findByParticipantOrCreator(Long userId);

    @Query("""
    SELECT DISTINCT r FROM CompanionChatRoom r
    JOIN r.participants p
    WHERE r.companion.id = :companionId AND p.id = :userId
""")
    List<CompanionChatRoom> findAllByCompanionIdAndParticipantId(Long companionId, Long userId);
    
    @Query("SELECT cr FROM CompanionChatRoom cr JOIN cr.participants p WHERE cr.id = :roomId AND p.id = :userId")
    Optional<CompanionChatRoom> findByIdAndParticipantId(@Param("roomId") Long roomId, @Param("userId") Long userId);
}

