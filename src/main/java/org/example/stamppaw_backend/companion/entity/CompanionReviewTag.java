package org.example.stamppaw_backend.companion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "companion_review_tags")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanionReviewTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag")
    private String tag;
}
