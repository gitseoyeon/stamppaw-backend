package org.example.stamppaw_backend.dog.repository;

import java.util.List;
import org.example.stamppaw_backend.dog.entity.Dog;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
    List<Dog> findAllByUserOrderByIdAsc(User user);
}
