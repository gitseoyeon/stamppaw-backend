package org.example.stamppaw_backend.companion.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.companion.dto.response.ChatRoomResponse;
import org.example.stamppaw_backend.companion.service.CompanionChatService;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companion/chat/rooms")
@RequiredArgsConstructor
public class CompanionChatRoomController {

    private final CompanionChatService chatService;

    @PostMapping("/companions/{companionId}")
    public ResponseEntity<ChatRoomResponse> getOrCreateChatRoom(
            @PathVariable Long companionId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        ChatRoomResponse response = chatService.getOrCreateChatRoom(companionId, userDetails.getUser().getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatRoomResponse> chatRooms = chatService.getChatRooms(userDetails.getUser().getId());
        for(ChatRoomResponse response : chatRooms) System.out.println(response);
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponse> getChatRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal User user) {
        ChatRoomResponse chatRoom = chatService.getChatRoom(roomId, user.getId());
        return ResponseEntity.ok(chatRoom);
    }

    // 추후 추가 기능
    @PostMapping("/{roomId}/participants/{participantId}")
    public ResponseEntity<String> addParticipant(
            @PathVariable Long roomId,
            @PathVariable Long participantId,
            @AuthenticationPrincipal User user) {
        chatService.addParticipant(roomId, participantId, user.getId());
        return ResponseEntity.ok("참여자가 추가되었습니다.");
    }
}
