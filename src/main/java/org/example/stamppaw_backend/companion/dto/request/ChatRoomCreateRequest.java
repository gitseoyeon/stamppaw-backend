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
public class ChatRoomCreateRequest {
    @NotNull(message = "동행 게시글 ID는 필수입니다")
    private Long companionId;
    
    @NotBlank(message = "채팅방 이름은 필수입니다")
    private String name;
    
    private Long participantId; // 초대할 사용자 ID (선택)
}

