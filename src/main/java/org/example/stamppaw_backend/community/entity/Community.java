package org.example.stamppaw_backend.community.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.stamppaw_backend.common.BasicTimeEntity;
import org.example.stamppaw_backend.community.dto.CommunityDto;
import org.example.stamppaw_backend.community.dto.request.CommunityModifyRequest;
import org.example.stamppaw_backend.companion.entity.RecruitmentStatus;
import org.example.stamppaw_backend.user.entity.User;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "community")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Community extends BasicTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String imageUrl;

    private Long views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Comment> comments = new HashSet<>();

    @Override
    public void setPrePersist() {
        super.setPrePersist();
        this.views = 0L;
    }

    public void updateViews(Long views) {
        this.views += views;
    }

    public void updateCommunity(CommunityDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        if(dto.getImage() != null) {
            this.imageUrl = dto.getImage();
        }
    }
}
