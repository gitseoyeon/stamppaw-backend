package org.example.stamppaw_backend.community.repository;

import org.example.stamppaw_backend.community.entity.Community;
import org.example.stamppaw_backend.community.entity.Like;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Long countByCommunityId(Long communityId);

    boolean existsByUserAndCommunity(User user, Community community);

    void deleteByUserAndCommunity(User user, Community community);
}
