package org.example.stamppaw_backend.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.user.dto.response.UserResponseDto;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    @GetMapping("/me")
    public UserResponseDto getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getMyInfo(userDetails);
    }

    // 내 정보 수정
    @PatchMapping(value = "/me")
    public UserResponseDto updateMyInfo(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestPart(value = "nickname", required = false) String nickname,
        @RequestPart(value = "bio", required = false) String bio,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        return userService.updateMyInfo(userDetails, nickname, bio, profileImage);
    }

}
