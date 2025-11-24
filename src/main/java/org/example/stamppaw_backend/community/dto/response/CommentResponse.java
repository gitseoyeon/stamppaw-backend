package org.example.stamppaw_backend.community.dto.response;

import lombok.*;
import org.example.stamppaw_backend.community.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private Long userId;
    private String nickname;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CommentResponse> children;

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .nickname(comment.getUser().getNickname())
                .profileImage(comment.getUser().getProfileImage())
                .createdAt(comment.getRegisteredAt())
                .updatedAt(comment.getModifiedAt())
                .children(
                        comment.getChildren().stream()
                                .map(CommentResponse::fromEntity)
                                .toList()
                )
                .build();
    }
}
