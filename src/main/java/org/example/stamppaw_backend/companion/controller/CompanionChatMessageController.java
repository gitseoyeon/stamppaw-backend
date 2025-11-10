package org.example.stamppaw_backend.companion.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.companion.dto.response.ChatMessageResponse;
import org.example.stamppaw_backend.companion.service.CompanionChatService;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companion/chat/messages")
@RequiredArgsConstructor
public class CompanionChatMessageController {

    private final CompanionChatService chatService;

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Page<ChatMessageResponse>> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatMessageResponse> messages = chatService.getMessages(roomId, userDetails.getUser().getId(), pageable);
        return ResponseEntity.ok(messages);
    }
}
