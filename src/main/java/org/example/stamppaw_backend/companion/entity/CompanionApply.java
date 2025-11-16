package org.example.stamppaw_backend.companion.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.common.BasicTimeEntity;
import org.example.stamppaw_backend.user.entity.User;

@Entity
@Table(name = "companion_apply")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanionApply extends BasicTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companion_id")
    private Companion companion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplyStatus status;

    @Override
    public void setPrePersist() {
        super.setPrePersist();
        this.status = ApplyStatus.PENDING;
    }

    public void changeStatus(ApplyStatus status) {
        this.status = status;
    }

}