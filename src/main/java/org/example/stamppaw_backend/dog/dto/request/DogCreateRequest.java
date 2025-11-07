package org.example.stamppaw_backend.dog.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class DogCreateRequest {
    private String name;
    private String breed;
    private int age;
    private String character;
    private String image_url;

    public DogCreateRequest(String name, String breed, int age, String character, String image_url) {
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.character = character;
        this.image_url = image_url;
    }
}
