package org.example.stamppaw_backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.repository.UserRepository;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("WebSocket CONNECT 에 Authorization 헤더가 없습니다.");
                throw new StampPawException(ErrorCode.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            if (!jwtTokenProvider.validateToken(token)) {
                log.warn("WebSocket JWT 토큰 검증 실패");
                throw new StampPawException(ErrorCode.INVALID_TOKEN);
            }

            String email = jwtTokenProvider.getEmail(token);
            String role = jwtTokenProvider.getRole(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            User user;
            if (userDetails instanceof CustomUserDetails cud) {
                user = cud.getUser();
            } else {
                user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new StampPawException(ErrorCode.USER_NOT_FOUND));
            }

            accessor.getSessionAttributes().put("user", user);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            List.of(new SimpleGrantedAuthority(role != null ? role : "ROLE_USER"))
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("WebSocket 인증 성공: {}", email);
        }

        return message;
    }
}