package org.example.stamppaw_backend.badge.init;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.badge.entity.Badge;
import org.example.stamppaw_backend.badge.entity.BadgeCode;
import org.example.stamppaw_backend.badge.repository.BadgeRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BadgeInitializer {

    private final BadgeRepository badgeRepository;

    @Bean
    public ApplicationRunner initBadges() {
        return args -> {

            // 이미 데이터가 있다면 실행 안 함
            if (badgeRepository.count() > 0) {
                System.out.println("⏭ BadgeInitializer: 이미 데이터가 있어 초기화 생략");
                return;
            }

            System.out.println("🚀 BadgeInitializer: 초기 뱃지 생성 시작");


            for (BadgeCode code : BadgeCode.values()) {

                if (badgeRepository.existsByBadgeCode(code)) continue;

                Badge badge = Badge.builder()
                        .badgeCode(code)
                        .name(null)
                        .description(null)
                        .iconUrl(null)
                        .category(null)
                        .active(true)
                        .build();

                badgeRepository.save(badge);
            }

            System.out.println("🎉 BadgeInitializer: 기본 뱃지 초기화 완료!");
        };
    }
}
