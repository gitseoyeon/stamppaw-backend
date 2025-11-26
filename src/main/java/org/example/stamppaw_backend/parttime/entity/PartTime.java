package org.example.stamppaw_backend.parttime.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.common.BasicTimeEntity;
import org.example.stamppaw_backend.companion.entity.RecruitmentStatus;
import org.example.stamppaw_backend.parttime.dto.PartTimeManageDto;
import org.example.stamppaw_backend.parttime.dto.response.PartTimeResponse;
import org.example.stamppaw_backend.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parttime")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartTime extends BasicTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private RecruitmentStatus status;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "partTime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartTimeApply> applies = new ArrayList<>();

    @Override
    public void setPrePersist() {
        super.setPrePersist();
        this.status = RecruitmentStatus.ONGOING;
    }

    public PartTimeResponse updatePartTime(PartTimeManageDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();

        if (dto.getImage() == null || dto.getImage().isBlank()) {
            // 이미지 삭제 or 이미지 없음
            this.imageUrl = null;
        } else {
            // 새 이미지 적용
            this.imageUrl = dto.getImage();
        }

        return PartTimeResponse.builder()
            .title(title)
            .content(content)
            .image(imageUrl)
            .build();
    }

    public void updateStatus(RecruitmentStatus status) {
        this.status = status;
    }
}
