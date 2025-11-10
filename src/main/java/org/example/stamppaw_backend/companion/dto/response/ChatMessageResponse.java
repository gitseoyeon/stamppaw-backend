package org.example.stamppaw_backend.companion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.companion.entity.CompanionChatMessage;
import org.example.stamppaw_backend.user.dto.response.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private Long id;
    private Long roomId;
    private UserDto sender;
    private String content;
    private String type;
    private LocalDateTime registeredAt;

    public static ChatMessageResponse fromEntity(CompanionChatMessage message) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .roomId(message.getChatRoom().getId())
                .sender(UserDto.fromEntity(message.getSender()))
                .content(message.getContent())
                .type(message.getType().name())
                .registeredAt(message.getRegisteredAt())
                .build();
    }
}

