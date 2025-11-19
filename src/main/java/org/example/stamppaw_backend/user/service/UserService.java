package org.example.stamppaw_backend.user.service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.S3Service;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;

import org.example.stamppaw_backend.dog.dto.response.DogResponse;
import org.example.stamppaw_backend.dog.repository.DogRepository;

import org.example.stamppaw_backend.user.dto.response.UserResponseDto;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DogRepository dogRepository;
    private final S3Service s3Service;

    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public User getUserOrException(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new StampPawException(ErrorCode.USER_NOT_FOUND));
    }

    public UserResponseDto getMyInfo(UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new StampPawException(ErrorCode.USER_NOT_FOUND));

        List<DogResponse> dogs = dogRepository.findAllByUserOrderByIdAsc(user)
            .stream()
            .map(DogResponse::from)
            .toList();


        int recordCount = 0;
        int followerCount = 0;
        int followingCount = 0;

        return new UserResponseDto(
            user.getId(),
            user.getNickname(),
            user.getEmail(),
            user.getRegion(),
            user.getBio(),
            user.getProfileImage(),
            recordCount,
            followerCount,
            followingCount,
            dogs
        );
    }

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

        userRepository.save(user);

        User updated = userRepository.findById(user.getId())
            .orElseThrow(() -> new StampPawException(ErrorCode.USER_NOT_FOUND));

        List<DogResponse> dogs = dogRepository.findAllByUserOrderByIdAsc(updated)
            .stream()
            .map(DogResponse::from)
            .toList();

        return new UserResponseDto(
            updated.getId(),
            updated.getNickname(),
            updated.getEmail(),
            updated.getRegion(),
            updated.getBio(),
            updated.getProfileImage(),
            0,
            0,
            0,
            dogs
        );
    }
}
