package org.example.stamppaw_backend.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stamppaw_backend.community.entity.Community;
import org.example.stamppaw_backend.user.dto.response.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Long views;
    private LocalDateTime registeredAt;
    private Long likeCount;
    private boolean isLiked;
    private Long commentCount;
    private UserDto user;

    public static CommunityResponse from(Community community) {
        return CommunityResponse.builder()
                .id(community.getId())
                .title(community.getTitle())
                .content(community.getContent())
                .imageUrl(community.getImageUrl())
                .views(community.getViews())
                .registeredAt(community.getRegisteredAt())
                .user(UserDto.fromEntity(community.getUser()))
                .build();
    }

    public static CommunityResponse fromEntity(Community community, Long totalViews, Long likeCount, boolean isLiked) {
        return CommunityResponse.builder()
                .id(community.getId())
                .title(community.getTitle())
                .content(community.getContent())
                .imageUrl(community.getImageUrl())
                .views(totalViews)
                .likeCount(likeCount)
                .isLiked(isLiked)
                .registeredAt(community.getRegisteredAt())
                .user(UserDto.fromEntity(community.getUser()))
                .build();
    }
}
