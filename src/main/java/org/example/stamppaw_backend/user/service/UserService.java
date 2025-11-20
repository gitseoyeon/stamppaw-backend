package org.example.stamppaw_backend.user.service;

import org.example.stamppaw_backend.follow.repository.FollowRepository;
import org.example.stamppaw_backend.dog.dto.response.DogResponse;
import org.example.stamppaw_backend.dog.repository.DogRepository;
import org.example.stamppaw_backend.user.dto.response.UserResponseDto;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.repository.UserRepository;

import org.example.stamppaw_backend.common.S3Service;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DogRepository dogRepository;
    private final S3Service s3Service;
    private final FollowRepository followRepository;

    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public User getUserOrException(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new StampPawException(ErrorCode.USER_NOT_FOUND));
    }

    /** 🔹 내 정보 조회 */
    public UserResponseDto getMyInfo(UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new StampPawException(ErrorCode.USER_NOT_FOUND));

        // 강아지 목록
        List<DogResponse> dogs = dogRepository.findAllByUserOrderByIdAsc(user)
            .stream()
            .map(DogResponse::from)
            .toList();

        // 팔로워 / 팔로잉 개수
        int followerCount = followRepository.findByFollowing(user).size();
        int followingCount = followRepository.findByFollower(user).size();

        // isFollowing = 내 정보에서는 null
        return UserResponseDto.of(user, followerCount, followingCount, dogs, null);
    }

    /** 🔧 내 정보 수정 */
    @Transactional
    public UserResponseDto updateMyInfo(
        UserDetails userDetails,
        String nickname,
        String bio,
        MultipartFile profileImage
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new StampPawException(ErrorCode.USER_NOT_FOUND));

        if (nickname != null && !nickname.equals(user.getNickname())) {
            if (userRepository.existsByNickname(nickname)) {
                throw new StampPawException(ErrorCode.DUPLICATE_NICKNAME);
            }
            user.setNickname(nickname);
        }

        if (bio != null) {
            user.setBio(bio);
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = s3Service.uploadFileAndGetUrl(profileImage);
            user.setProfileImage(imageUrl);
        }

        // 수정 후 강아지 목록 다시 조회
        List<DogResponse> dogs = dogRepository.findAllByUserOrderByIdAsc(user)
            .stream()
            .map(DogResponse::from)
            .toList();

        int followerCount = followRepository.findByFollowing(user).size();
        int followingCount = followRepository.findByFollower(user).size();

        return UserResponseDto.of(user, followerCount, followingCount, dogs, null);
    }

    /** 🔍 다른 유저 프로필 조회 */
    public UserResponseDto getUserProfile(User me, Long id) {

        User target = userRepository.findById(id)
            .orElseThrow(() -> new StampPawException(ErrorCode.USER_NOT_FOUND));

        // 강아지 목록
        List<DogResponse> dogs = dogRepository.findAllByUserOrderByIdAsc(target)
            .stream()
            .map(DogResponse::from)
            .toList();

        int followerCount = followRepository.findByFollowing(target).size();
        int followingCount = followRepository.findByFollower(target).size();

        // 내가 이 유저를 팔로우했는지 여부
        boolean isFollowing = followRepository
            .findByFollowerAndFollowing(me, target)
            .isPresent();

        return UserResponseDto.of(target, followerCount, followingCount, dogs, isFollowing);
    }
}
