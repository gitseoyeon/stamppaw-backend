package org.example.stamppaw_backend.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.user.dto.request.UserUpdateRequest;
import org.example.stamppaw_backend.user.dto.response.UserResponseDto;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    @GetMapping("/me")
    public UserResponseDto getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getMyInfo(userDetails);
    }

    // 내 정보 수정
    @PatchMapping("/me")
    public UserResponseDto updateMyInfo(@AuthenticationPrincipal UserDetails userDetails,
        @RequestBody UserUpdateRequest request) {
        return userService.updateMyInfo(userDetails, request);
    }
}
