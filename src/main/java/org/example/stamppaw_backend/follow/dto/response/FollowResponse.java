package org.example.stamppaw_backend.follow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.stamppaw_backend.follow.entity.Follow;
import org.example.stamppaw_backend.user.entity.User;

@Getter
@Builder
@AllArgsConstructor
public class FollowResponse {

    private Long id;
    private String nickname;
    private String profileImage;
    private String bio;
    private boolean isFollowing;
    private boolean isFollower;

    // 팔로잉 목록 (내가 팔로우한 사람)
    public static FollowResponse from(Follow follow) {
        User user = follow.getFollowing(); // 내가 팔로우한 대상

        return FollowResponse.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .profileImage(user.getProfileImage())
            .bio(user.getBio())
            .isFollowing(true)
            .build();
    }

    // 팔로워 목록 (나를 팔로우한 사람)
    public static FollowResponse fromFollowerView(Follow follow) {
        User user = follow.getFollower();

        return FollowResponse.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .profileImage(user.getProfileImage())
            .bio(user.getBio())
            .isFollowing(true)
            .build();
    }
}
