package org.example.stamppaw_backend.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.user.dto.request.UserUpdateRequest;
import org.example.stamppaw_backend.user.dto.response.UserResponseDto;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserOrExcepion(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new StampPawException(ErrorCode.USER_NOT_FOUND));

    }

    public UserResponseDto getMyInfo(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new StampPawException(ErrorCode.USER_NOT_FOUND));
        return new UserResponseDto(
            user.getId(),
            user.getNickname(),
            user.getEmail(),
            user.getRegion(),
            user.getBio()
        );
    }


    @Transactional
    public UserResponseDto updateMyInfo(UserDetails userDetails, UserUpdateRequest request) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new StampPawException(ErrorCode.USER_NOT_FOUND));

        if (request.getNickname() != null) user.setNickname(request.getNickname());
        if (request.getRegion() != null) user.setRegion(request.getRegion());
        if (request.getBio() != null) user.setBio(request.getBio());

        userRepository.save(user);

        return new UserResponseDto(
            user.getId(),
            user.getNickname(),
            user.getEmail(),
            user.getRegion(),
            user.getBio()
        );
    }
}

