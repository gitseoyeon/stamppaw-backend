package org.example.stamppaw_backend.companion.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.companion.dto.request.ChatMessageRequest;
import org.example.stamppaw_backend.companion.service.CompanionChatService;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageWebSocketController {

    private final CompanionChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor) {
        try {
            Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
            if (sessionAttributes == null) {
                log.error("WebSocket 세션 속성이 비어 있습니다.");
                return;
            }

            User user = (User) sessionAttributes.get("user");
            if (user == null) {
                log.error("WebSocket 세션에서 사용자 정보를 찾을 수 없습니다.");
                return;
            }

            chatService.sendMessage(request, user.getId());
            log.info("✅ 메시지 전송 완료: roomId={}, senderId={}", request.getRoomId(), user.getId());
        } catch (Exception e) {
            log.error("메시지 전송 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}


