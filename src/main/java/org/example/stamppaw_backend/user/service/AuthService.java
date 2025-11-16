package org.example.stamppaw_backend.user.service;


import lombok.RequiredArgsConstructor;
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

        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 새 유저 생성
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
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());

        return LoginResponse.builder()
                .token(token)
                .user(UserDto.fromEntity(user))
                .build();
    }
}

