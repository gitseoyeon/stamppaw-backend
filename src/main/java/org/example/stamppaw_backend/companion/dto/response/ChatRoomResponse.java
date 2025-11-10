package org.example.stamppaw_backend.companion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.companion.entity.CompanionChatRoom;
import org.example.stamppaw_backend.user.dto.response.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {
    private Long id;
    private Long companionId;
    private String companionTitle;
    private String name;
    private UserDto creator;
    private List<UserDto> participants;
    private LocalDateTime registeredAt;
    private LocalDateTime modifiedAt;
    private Long lastMessageId;
    private String lastMessageContent;
    private LocalDateTime lastMessageTime;
    private Long lastMessageSenderId;

    public static ChatRoomResponse fromEntity(CompanionChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .companionId(chatRoom.getCompanion().getId())
                .companionTitle(chatRoom.getCompanion().getTitle())
                .name(chatRoom.getName())
                .creator(UserDto.fromEntity(chatRoom.getCreator()))
                .participants(chatRoom.getParticipants().stream()
                        .map(UserDto::fromEntity)
                        .collect(Collectors.toList()))
                .registeredAt(chatRoom.getRegisteredAt())
                .modifiedAt(chatRoom.getModifiedAt())
                .build();
    }

    public static ChatRoomResponse fromEntityWithLastMessage(CompanionChatRoom chatRoom) {
        ChatRoomResponse response = fromEntity(chatRoom);
        
        if (chatRoom.getMessages() != null && !chatRoom.getMessages().isEmpty()) {
            var lastMessage = chatRoom.getMessages().get(chatRoom.getMessages().size() - 1);
            response.setLastMessageId(lastMessage.getId());
            response.setLastMessageContent(lastMessage.getContent());
            response.setLastMessageTime(lastMessage.getRegisteredAt());
            response.setLastMessageSenderId(lastMessage.getSender().getId());
        }
        
        return response;
    }
}

