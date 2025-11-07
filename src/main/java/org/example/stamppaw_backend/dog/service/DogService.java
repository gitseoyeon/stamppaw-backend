package org.example.stamppaw_backend.dog.service;

import java.util.List;
import java.util.stream.Collectors;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.dog.dto.request.DogCreateRequest;
import org.example.stamppaw_backend.dog.dto.request.DogUpdateRequest;
import org.example.stamppaw_backend.dog.dto.response.DogResponse;
import org.example.stamppaw_backend.dog.entity.Dog;
import org.example.stamppaw_backend.dog.repository.DogRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DogService {

    private final DogRepository dogRepository;

    @Transactional
    public DogResponse createDog(User user, DogCreateRequest request) {
        Dog dog = Dog.builder()
            .user(user)
            .name(request.getName())
            .breed(request.getBreed())
            .age(request.getAge())
            .character(request.getCharacter())
            .image_url(request.getImage_url())
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
    public DogResponse updateDog(Long dogId, DogUpdateRequest request) {
        Dog dog = dogRepository.findById(dogId)
            .orElseThrow(() -> new StampPawException(ErrorCode.DOG_NOT_FOUND));

        dog.updateDog(
            request.getName(),
            request.getBreed(),
            request.getAge(),
            request.getCharacter(),
            request.getImage_url()
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
