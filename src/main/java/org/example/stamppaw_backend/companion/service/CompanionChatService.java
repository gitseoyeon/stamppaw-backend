package org.example.stamppaw_backend.companion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.companion.dto.request.ChatMessageRequest;
import org.example.stamppaw_backend.companion.dto.response.ChatMessageResponse;
import org.example.stamppaw_backend.companion.dto.response.ChatRoomResponse;
import org.example.stamppaw_backend.companion.entity.Companion;
import org.example.stamppaw_backend.companion.entity.CompanionChatMessage;
import org.example.stamppaw_backend.companion.entity.CompanionChatRoom;
import org.example.stamppaw_backend.companion.repository.CompanionChatMessageRepository;
import org.example.stamppaw_backend.companion.repository.CompanionChatRoomRepository;
import org.example.stamppaw_backend.companion.repository.CompanionRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CompanionChatService {

    private final CompanionChatRoomRepository chatRoomRepository;
    private final CompanionChatMessageRepository chatMessageRepository;
    private final CompanionRepository companionRepository;
    private final UserService userService;
    private final RedisPublisher redisPublisher;

    public ChatRoomResponse getOrCreateChatRoom(Long companionId, Long userId) {
        User currentUser = userService.getUserOrException(userId);
        Companion companion = companionRepository.findById(companionId)
                .orElseThrow(() -> new StampPawException(ErrorCode.NOT_FOUND));

        User companionWriter = companion.getUser();

        List<CompanionChatRoom> existingRooms =
                chatRoomRepository.findAllByCompanionIdAndParticipantId(companionId, userId);

        if (!existingRooms.isEmpty()) {
            CompanionChatRoom chatRoom = existingRooms.get(0);

            boolean alreadyParticipant = chatRoom.getParticipants().stream()
                    .anyMatch(p -> p.getId().equals(currentUser.getId()));

            if (!alreadyParticipant) {
                chatRoom.addParticipant(currentUser);
                chatRoomRepository.save(chatRoom);
            }


            return ChatRoomResponse.fromEntity(chatRoom);
        }

        if (currentUser.getId().equals(companionWriter.getId())) {
            throw new StampPawException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 새 채팅방 생성
        String roomName = companion.getTitle() + " 채팅방";

        CompanionChatRoom chatRoom = CompanionChatRoom.builder()
                .companion(companion)
                .creator(companionWriter)
                .name(roomName)
                .build();

        chatRoom.addParticipant(companionWriter);
        chatRoom.addParticipant(currentUser);

        chatRoom = chatRoomRepository.save(chatRoom);

        return ChatRoomResponse.fromEntity(chatRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRooms(Long userId) {
        List<CompanionChatRoom> chatRooms = chatRoomRepository.findByParticipantOrCreator(userId);
        return chatRooms.stream()
                .map(ChatRoomResponse::fromEntityWithLastMessage)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChatRoomResponse getChatRoom(Long roomId, Long userId) {
        CompanionChatRoom chatRoom = chatRoomRepository.findByIdAndParticipantId(roomId, userId)
                .orElseThrow(() -> new StampPawException(ErrorCode.NOT_FOUND));
        return ChatRoomResponse.fromEntity(chatRoom);
    }

    public ChatMessageResponse sendMessage(ChatMessageRequest request, Long userId) {
        User sender = userService.getUserOrException(userId);
        CompanionChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new StampPawException(ErrorCode.NOT_FOUND));

        // 채팅방 참여자 확인
        boolean isParticipant = chatRoom.getParticipants().stream()
                .anyMatch(p -> p.getId().equals(userId));
        if (!isParticipant) {
            throw new StampPawException(ErrorCode.FORBIDDEN_ACCESS);
        }

        CompanionChatMessage.MessageType messageType = CompanionChatMessage.MessageType.TALK;
        if (request.getType() != null) {
            try {
                messageType = CompanionChatMessage.MessageType.valueOf(request.getType());
            } catch (IllegalArgumentException e) {
                log.warn("잘못된 메시지 타입: {}", request.getType());
            }
        }

        CompanionChatMessage message = CompanionChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(request.getContent())
                .type(messageType)
                .build();

        message = chatMessageRepository.save(message);

        ChatMessageResponse response = ChatMessageResponse.fromEntity(message);

        // Redis에 메시지 발행
        redisPublisher.publish(response);

        return response;
    }

    @Transactional(readOnly = true)
    public Page<ChatMessageResponse> getMessages(Long roomId, Long userId, Pageable pageable) {
        CompanionChatRoom chatRoom = chatRoomRepository.findByIdAndParticipantId(roomId, userId)
                .orElseThrow(() -> new StampPawException(ErrorCode.NOT_FOUND));

        Page<CompanionChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByRegisteredAtDesc(roomId, pageable);
        return messages.map(ChatMessageResponse::fromEntity);
    }

    public void addParticipant(Long roomId, Long participantId, Long userId) {
        CompanionChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new StampPawException(ErrorCode.NOT_FOUND));

        // 채팅방 생성자만 참여자 추가 가능
        if (!chatRoom.getCreator().getId().equals(userId)) {
            throw new StampPawException(ErrorCode.FORBIDDEN_ACCESS);
        }

        User participant = userService.getUserOrException(participantId);
        chatRoom.addParticipant(participant);
        chatRoomRepository.save(chatRoom);
    }
}
