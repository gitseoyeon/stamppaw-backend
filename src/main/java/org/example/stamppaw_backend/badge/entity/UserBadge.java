package org.example.stamppaw_backend.badge.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private Badge badge;

    private boolean achieved;

    private LocalDateTime achievedAt;

    private int progress;

    private boolean representative;

    public void updateProgress(int progress) {
        this.progress = progress;
    }

    public void achieve() {
        this.achieved = true;
        this.achievedAt = LocalDateTime.now();
        this.progress = 100;
    }

    public void cancelRepresentative() {
        this.representative = false;
    }

    public void setRepresentative() {
        this.representative = true;
    }
}
