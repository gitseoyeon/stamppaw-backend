package org.example.stamppaw_backend.community.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.common.BasicTimeEntity;
import org.example.stamppaw_backend.user.entity.User;

@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like extends BasicTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;
}
