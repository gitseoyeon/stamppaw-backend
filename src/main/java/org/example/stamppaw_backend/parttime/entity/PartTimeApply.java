package org.example.stamppaw_backend.parttime.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.common.BasicTimeEntity;
import org.example.stamppaw_backend.companion.entity.ApplyStatus;
import org.example.stamppaw_backend.user.entity.User;

@Entity
@Table(name = "parttime_apply")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartTimeApply extends BasicTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parttime_id")
    private PartTime partTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplyStatus status;

    @Override
    public void setPrePersist() {
        super.setPrePersist();
        this.status = ApplyStatus.PENDING;  // 기본값: 대기중
    }

    public void changeStatus(ApplyStatus status) {
        this.status = status;
    }
}
