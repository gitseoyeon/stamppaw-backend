package org.example.stamppaw_backend.companion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.common.BasicTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companion_reviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanionReview extends BasicTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_id", nullable = false)
    private CompanionApply apply;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanionReviewMapping> tags = new ArrayList<>();
}
