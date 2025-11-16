package org.example.stamppaw_backend.admin.mission.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.user_mission.entity.MissionType;

@Entity
@Table(name = "missions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int point;

    @Enumerated(EnumType.STRING)
    private MissionType type;

    private int targetDistance;
    private int targetTime;
}
