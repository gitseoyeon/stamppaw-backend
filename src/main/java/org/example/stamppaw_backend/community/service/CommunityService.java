package org.example.stamppaw_backend.community.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.S3Service;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.community.dto.CommunityDto;
import org.example.stamppaw_backend.community.dto.request.CommunityCreateRequest;
import org.example.stamppaw_backend.community.dto.request.CommunityModifyRequest;
import org.example.stamppaw_backend.community.dto.response.CommunityResponse;
import org.example.stamppaw_backend.community.entity.Community;
import org.example.stamppaw_backend.community.repository.CommunityRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final UserService userService;
    private final S3Service s3Service;
    private final CommunityRedisService communityRedisService;
    private final LikeService likeService;
    private final CommentService commentService;

    public void createCommunity(CommunityCreateRequest request, Long userId) {
        User user = userService.getUserOrException(userId);
        communityRepository.save(Community.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(request.getImage() != null ? s3Service.uploadFileAndGetUrl(request.getImage()) : null)
                .user(user)
                .build());
    }

    @Transactional(readOnly = true)
    public Page<CommunityResponse> getCommunities(Pageable pageable) {
        Page<Community> communities = communityRepository.findAll(pageable);
        return mapToCommunity(communities);
    }

    public CommunityResponse getCommunity(Long id, HttpServletRequest request, Long userId) {

        String sessionId = request.getSession(true).getId();
        communityRedisService.increaseView(id, sessionId);

        Community community = getCommunityOrException(id);
        Long redisViews = communityRedisService.getViewCount(id);
        Long total = community.getViews() + redisViews;

        Long likeCount = likeService.getLikeCount(id);

        boolean isLiked = false;
        if (userId != null) {
            isLiked = likeService.isLiked(userService.getUserOrException(userId), community);
        }

        return CommunityResponse.fromEntity(community, total, likeCount, isLiked);
    }

    public void modifyCommunity(Long id, CommunityModifyRequest request, Long userId) {
        User user = userService.getUserOrException(userId);
        Community community = getCommunityOrException(id);
        verifyUser(user, community);
        String imageUrl = request.getImage() != null ? s3Service.uploadFileAndGetUrl(request.getImage()) : null;

        community.updateCommunity(CommunityDto.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .image(imageUrl)
                .build());
    }

    public void deleteCommunity(Long id, Long userId) {
        User user = userService.getUserOrException(userId);
        Community community = getCommunityOrException(id);
        verifyUser(user, community);

        communityRepository.delete(community);
    }

    public boolean toggleLike(Long communityId, Long userId) {
        User user = userService.getUserOrException(userId);
        Community community = getCommunityOrException(communityId);

        return likeService.toggleLike(user, community);
    }

    public Community getCommunityOrException(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new StampPawException(ErrorCode.COMMUNITY_NOT_FOUND));
    }

    private void verifyUser(User user, Community community) {
        if(!community.getUser().equals(user)) {
            throw new StampPawException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }

    private Page<CommunityResponse> mapToCommunity(Page<Community> communities) {
        return communities.map(community -> {
            CommunityResponse response = CommunityResponse.from(community);
            Long likeCount = likeService.getLikeCount(community.getId());
            Long commentCount = commentService.getCommentCount(community.getId());

            response.setLikeCount(likeCount);
            response.setCommentCount(commentCount);
            return response;
        });
    }
}
