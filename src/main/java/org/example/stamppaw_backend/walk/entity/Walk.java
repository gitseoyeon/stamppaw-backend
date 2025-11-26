package org.example.stamppaw_backend.walk.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "walks")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Walk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long duration;
    private Double distance;

    private Double startLat;
    private Double startLng;
    private Double endLat;
    private Double endLng;

    @OneToMany(mappedBy = "walk", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WalkPoint> points = new ArrayList<>();

    @Column(length = 1000)
    private String memo;

    @OneToMany(mappedBy = "walk", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WalkPhoto> photos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private WalkStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
