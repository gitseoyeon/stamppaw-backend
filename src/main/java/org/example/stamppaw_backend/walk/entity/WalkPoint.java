package org.example.stamppaw_backend.walk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "walk_points",
        indexes = {
                @Index(name = "idx_walkpoint_walk_id", columnList = "walk_id"),
                @Index(name = "idx_walkpoint_timestamp", columnList = "timestamp")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_id", nullable = false)
    private Walk walk;
}