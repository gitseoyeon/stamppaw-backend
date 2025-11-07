package org.example.stamppaw_backend.dog.controller;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.dog.dto.request.DogCreateRequest;
import org.example.stamppaw_backend.dog.dto.request.DogUpdateRequest;
import org.example.stamppaw_backend.dog.dto.response.DogResponse;
import org.example.stamppaw_backend.dog.service.DogService;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dogs")
public class DogController {

    private final DogService dogService;

    @PostMapping
    public ResponseEntity<DogResponse> createDog(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody DogCreateRequest request) {

        User user = userDetails.getUser();
        DogResponse created = dogService.createDog(user, request);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<DogResponse>> getAllDogs(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        List<DogResponse> dogs = dogService.getAllDogs(user);
        return ResponseEntity.ok(dogs);
    }


    @GetMapping("/{dogId}")
    public ResponseEntity<DogResponse> getDogById(@PathVariable Long dogId) {
        DogResponse dog = dogService.getDogById(dogId);
        return ResponseEntity.ok(dog);
    }

    @PatchMapping("/{dogId}")
    public ResponseEntity<DogResponse> updateDog(
        @PathVariable Long dogId,
        @RequestBody DogUpdateRequest request) {

        DogResponse updatedDog = dogService.updateDog(dogId, request);
        return ResponseEntity.ok(updatedDog);
    }

    @DeleteMapping("/{dogId}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long dogId) {
        dogService.deleteDog(dogId);
        return ResponseEntity.noContent().build();
    }
}
