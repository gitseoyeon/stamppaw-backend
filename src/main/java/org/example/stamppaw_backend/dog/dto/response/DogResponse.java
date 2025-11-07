package org.example.stamppaw_backend.dog.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.stamppaw_backend.dog.entity.Dog;

@Getter
@Builder
@AllArgsConstructor
public class DogResponse {

    private Long id;
    private String name;
    private String breed;
    private int age;
    private String character;
    private String image_Url;

    public static DogResponse from(Dog dog) {
        return DogResponse.builder()
                .id(dog.getId())
                .name(dog.getName())
                .breed(dog.getBreed())
                .age(dog.getAge())
                .character(dog.getCharacter())
                .image_Url(dog.getImage_url())
                .build();
    }
}
