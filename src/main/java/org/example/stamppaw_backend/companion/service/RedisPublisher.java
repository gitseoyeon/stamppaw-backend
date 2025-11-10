package org.example.stamppaw_backend.companion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.companion.dto.response.ChatMessageResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    public void publish(ChatMessageResponse message) {
        try {
            redisTemplate.convertAndSend(channelTopic.getTopic(), message);
            log.debug("Redis 메시지 발행 성공: roomId={}", message.getRoomId());
        } catch (Exception e) {
            log.error("Redis 메시지 발행 실패: {}", e.getMessage(), e);
        }
    }
}


