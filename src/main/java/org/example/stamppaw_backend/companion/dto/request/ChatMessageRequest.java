package org.example.stamppaw_backend.companion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
    @NotNull(message = "채팅방 ID는 필수입니다")
    private Long roomId;
    
    @NotBlank(message = "메시지 내용은 필수입니다")
    private String content;
    
    private String type; // TALK, ENTER, LEAVE
}

