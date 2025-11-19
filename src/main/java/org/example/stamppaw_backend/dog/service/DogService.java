package org.example.stamppaw_backend.dog.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.S3Service;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.dog.dto.request.DogCreateRequest;
import org.example.stamppaw_backend.dog.dto.request.DogUpdateRequest;
import org.example.stamppaw_backend.dog.dto.response.DogResponse;
import org.example.stamppaw_backend.dog.entity.Dog;
import org.example.stamppaw_backend.dog.repository.DogRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DogService {

    private final DogRepository dogRepository;
    private final S3Service s3Service;

    @Transactional
    public DogResponse createDog(User user, DogCreateRequest request, MultipartFile image) {

        // 요청에 image_url이 있으면 기본값으로 사용
        String image_url = request.getImage_url();

        // 파일이 업로드되었으면 S3에 저장 후 URL 덮어쓰기
        if (image != null && !image.isEmpty()) {
            image_url = s3Service.uploadFileAndGetUrl(image);
        }

        Dog dog = Dog.builder()
            .user(user)
            .name(request.getName())
            .breed(request.getBreed())
            .age(request.getAge())
            .character(request.getCharacter())
            .image_url(image_url)
            .build();

        dogRepository.save(dog);
        return DogResponse.from(dog);
    }

    public List<DogResponse> getAllDogs(User user) {
        return dogRepository.findAllByUserOrderByIdAsc(user).stream()
            .map(DogResponse::from)
            .collect(Collectors.toList());
    }

    public DogResponse getDogById(Long dogId) {
        Dog dog = dogRepository.findById(dogId)
            .orElseThrow(() -> new StampPawException(ErrorCode.DOG_NOT_FOUND));
        return DogResponse.from(dog);
    }

    @Transactional
    public DogResponse updateDog(Long dogId, DogUpdateRequest request, MultipartFile image) {

        Dog dog = dogRepository.findById(dogId)
            .orElseThrow(() -> new StampPawException(ErrorCode.DOG_NOT_FOUND));

        // 기본값: 기존 이미지 유지
        String image_url = dog.getImage_url();

        // 새 이미지가 들어오면 S3에 업로드 후 URL 변경
        if (image != null && !image.isEmpty()) {
            image_url = s3Service.uploadFileAndGetUrl(image);
        }

        dog.updateDog(
            request.getName(),
            request.getBreed(),
            request.getAge(),
            request.getCharacter(),
            image_url
        );

        return DogResponse.from(dog);
    }

    @Transactional
    public void deleteDog(Long dogId) {
        Dog dog = dogRepository.findById(dogId)
            .orElseThrow(() -> new StampPawException(ErrorCode.DOG_NOT_FOUND));
        dogRepository.delete(dog);
    }
}
