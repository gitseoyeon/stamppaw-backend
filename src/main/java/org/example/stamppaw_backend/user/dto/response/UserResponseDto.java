package org.example.stamppaw_backend.user.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.stamppaw_backend.dog.dto.response.DogResponse;

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

    private List<DogResponse> dogs;
}
