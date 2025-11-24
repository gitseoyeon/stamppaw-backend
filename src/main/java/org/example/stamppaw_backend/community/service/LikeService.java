package org.example.stamppaw_backend.community.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.community.entity.Community;
import org.example.stamppaw_backend.community.entity.Like;
import org.example.stamppaw_backend.community.repository.LikeRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;

    public boolean toggleLike(User user, Community community) {
        if(isLiked(user, community)) {
            likeRepository.deleteByUserAndCommunity(user, community);
            return false;
        } else {
            Like like = Like.builder()
                    .user(user)
                    .community(community)
                    .build();

            likeRepository.save(like);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public Long getLikeCount(Long communityId) {
        return likeRepository.countByCommunityId(communityId);
    }

    @Transactional(readOnly = true)
    public boolean isLiked(User user, Community community) {
        return likeRepository.existsByUserAndCommunity(user, community);
    }
}
