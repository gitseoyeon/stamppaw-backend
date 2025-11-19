package org.example.stamppaw_backend.dog.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.dog.dto.request.DogCreateRequest;
import org.example.stamppaw_backend.dog.dto.request.DogUpdateRequest;
import org.example.stamppaw_backend.dog.dto.response.DogResponse;
import org.example.stamppaw_backend.dog.service.DogService;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dogs")
public class DogController {

    private final DogService dogService;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DogResponse> createDog(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestPart("data") DogCreateRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        User user = userDetails.getUser();
        DogResponse created = dogService.createDog(user, request, image);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<DogResponse>> getAllDogs(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        List<DogResponse> dogs = dogService.getAllDogs(user);
        return ResponseEntity.ok(dogs);
    }

    @GetMapping("/{dogId}")
    public ResponseEntity<DogResponse> getDogById(@PathVariable Long dogId) {
        DogResponse dog = dogService.getDogById(dogId);
        return ResponseEntity.ok(dog);
    }

    @PatchMapping(value = "/{dogId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DogResponse> updateDog(
        @PathVariable Long dogId,
        @RequestPart("data") DogUpdateRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        DogResponse updatedDog = dogService.updateDog(dogId, request, image);
        return ResponseEntity.ok(updatedDog);
    }

    @DeleteMapping("/{dogId}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long dogId) {
        dogService.deleteDog(dogId);
        return ResponseEntity.noContent().build();
    }
}
