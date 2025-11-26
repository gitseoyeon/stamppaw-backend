package org.example.stamppaw_backend.community.repository;

import org.example.stamppaw_backend.community.entity.Community;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Page<Community> findByUser(Pageable pageable, User user);
}
