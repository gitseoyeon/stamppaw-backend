package org.example.stamppaw_backend.user.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.config.JwtTokenProvider;
import org.example.stamppaw_backend.user.dto.request.LoginRequest;
import org.example.stamppaw_backend.user.dto.request.SignupRequest;
import org.example.stamppaw_backend.user.dto.response.LoginResponse;
import org.example.stamppaw_backend.user.dto.response.UserDto;
import org.example.stamppaw_backend.user.entity.Role;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String signup(SignupRequest request) {

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new StampPawException(ErrorCode.DUPLICATE_NICKNAME);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new StampPawException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
            .email(request.getEmail())
            .password(encodedPassword)
            .nickname(request.getNickname())
            .role(Role.USER)
            .build();

        userRepository.save(user);

        return "회원가입이 완료되었습니다.";
    }

    // 로그인
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new StampPawException(ErrorCode.AUTH_USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new StampPawException(ErrorCode.INVALID_REQUEST);
        }

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());

        return LoginResponse.builder()
            .token(token)
            .user(UserDto.fromEntity(user))
            .build();
    }
}
