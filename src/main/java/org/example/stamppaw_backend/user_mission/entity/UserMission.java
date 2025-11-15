package org.example.stamppaw_backend.user_mission.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.user.entity.User;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean status;
}