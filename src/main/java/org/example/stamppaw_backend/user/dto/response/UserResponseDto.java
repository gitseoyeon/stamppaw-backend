package org.example.stamppaw_backend.user.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.stamppaw_backend.dog.dto.response.DogResponse;
import org.example.stamppaw_backend.user.entity.User;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String nickname;
    private String email;
    private String region;
    private String bio;
    private String profileImage;

    private int recordCount;
    private int followerCount;
    private int followingCount;
    private Boolean isFollowing;

    private List<DogResponse> dogs;

    public static UserResponseDto of(
        User user,
        int followerCount,
        int followingCount,
        List<DogResponse> dogs,
        Boolean isFollowing
    ) {
        return new UserResponseDto(
            user.getId(),
            user.getNickname(),
            user.getEmail(),
            user.getRegion(),
            user.getBio(),
            user.getProfileImage(),
            0, // recordCount (나중에 추가될 부분)
            followerCount,
            followingCount,
            isFollowing,
            dogs
        );
    }
}
