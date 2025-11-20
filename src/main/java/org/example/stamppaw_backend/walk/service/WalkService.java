package org.example.stamppaw_backend.walk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stamppaw_backend.common.S3Service;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.admin.mission.service.MissionProcessor;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.walk.dto.request.*;
import org.example.stamppaw_backend.walk.dto.response.*;
import org.example.stamppaw_backend.walk.entity.*;
import org.example.stamppaw_backend.walk.repository.WalkPointRepository;
import org.example.stamppaw_backend.walk.repository.WalkRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalkService {

    private final WalkRepository walkRepository;
    private final WalkPointRepository walkPointRepository;
    private final WalkMapService walkMapService;
    private final S3Service s3Service;
    private final MissionProcessor missionProcessor;

    @Transactional
    public WalkStartResponse startWalk(WalkStartRequest request, User currentUser) {
        if (request.getLat() == null || request.getLng() == null) {
            throw new StampPawException(ErrorCode.INVALID_PARAMETER);
        }

        Walk walk = Walk.builder()
                .user(currentUser)
                .startLat(request.getLat())
                .startLng(request.getLng())
                .startTime(LocalDateTime.now())
                .status(WalkStatus.STARTED)
                .build();

        walk = walkRepository.save(walk);

        walkMapService.addPoint(walk.getId(), request.getLat(), request.getLng(), request.getTimestamp());

        return WalkStartResponse.fromEntity(walk);
    }

    @Transactional
    public WalkEndResponse endWalk(Long walkId, WalkEndRequest request) {
        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new StampPawException(ErrorCode.WALK_NOT_FOUND));

        if (walk.getStatus() != WalkStatus.STARTED) {
            throw new StampPawException(ErrorCode.INVALID_WALK_STATUS);
        }

        walk.setEndLat(request.getLat());
        walk.setEndLng(request.getLng());
        walk.setEndTime(LocalDateTime.now());
        walk.setMemo(request.getMemo());
        walk.setStatus(WalkStatus.ENDED);

        walkMapService.addPoint(walkId, request.getLat(), request.getLng(), request.getTimestamp());

        List<WalkPointResponse> points = walkPointRepository.findByWalkIdOrderByTimestampAsc(walkId)
                .stream()
                .map(WalkPointResponse::fromEntity)
                .toList();

        double totalDistance = walkMapService.calcTotalDistance(points);
        walk.setDistance(totalDistance);

        if (walk.getStartTime() != null && walk.getEndTime() != null) {
            long duration = Duration.between(walk.getStartTime(), walk.getEndTime()).getSeconds();
            walk.setDuration(duration);
        }

        walkRepository.save(walk);
        missionProcessor.handleWalkCompleted(walk);

        return WalkEndResponse.fromEntity(walk);
    }

    @Transactional
    public WalkResponse editWalk(Long walkId, WalkRecordRequest request, User currentUser) {
        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new StampPawException(ErrorCode.WALK_NOT_FOUND));

        if (!walk.getUser().getId().equals(currentUser.getId())) {
            throw new StampPawException(ErrorCode.UNAUTHORIZED_WALK_EDIT);
        }

        Optional.ofNullable(request.getMemo())
                .filter(m -> !m.isBlank())
                .ifPresent(walk::setMemo);

        if (request.getPhotos() != null && !request.getPhotos().isEmpty()) {
            walk.getPhotos().clear();

            List<String> uploadedUrls = s3Service.uploadFilesAndGetUrls(request.getPhotos());
            uploadedUrls.forEach(url -> {
                WalkPhoto newPhoto = new WalkPhoto();
                newPhoto.setPhotoUrl(url);
                newPhoto.setWalk(walk);
                walk.getPhotos().add(newPhoto);
            });
        }

        walk.setStatus(WalkStatus.RECORDED);
        walkRepository.save(walk);

        return buildWalkResponse(walk);
    }

    @Transactional(readOnly = true)
    public WalkResponse getWalkDetail(Long walkId, User currentUser) {
        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new StampPawException(ErrorCode.WALK_NOT_FOUND));

        if (!walk.getUser().getId().equals(currentUser.getId())) {
            throw new StampPawException(ErrorCode.UNAUTHORIZED_WALK_ACCESS);
        }

        return buildWalkResponse(walk);
    }

    @Transactional(readOnly = true)
    public Page<WalkResponse> getUserWalks(User currentUser, Pageable pageable) {
        Page<Walk> walksPage = walkRepository.findAllByUserIdAndStatusOrderByStartTimeDesc(
                currentUser.getId(), WalkStatus.RECORDED, pageable);

        if (walksPage.isEmpty()) {
            throw new StampPawException(ErrorCode.WALK_NOT_FOUND);
        }

        return walksPage.map(this::buildWalkResponse);
    }

    @Transactional
    public void deleteWalk(Long walkId, User currentUser) {
        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new StampPawException(ErrorCode.WALK_NOT_FOUND));

        if (!walk.getUser().getId().equals(currentUser.getId())) {
            throw new StampPawException(ErrorCode.UNAUTHORIZED_WALK_DELETE);
        }

        walkRepository.delete(walk);
        log.info("🗑️ Walk deleted successfully: id={}", walkId);
    }

    private WalkResponse buildWalkResponse(Walk walk) {
        List<WalkPointResponse> points = walkPointRepository.findByWalkIdOrderByTimestampAsc(walk.getId())
                .stream()
                .map(WalkPointResponse::fromEntity)
                .toList();

        List<String> photoUrls = Optional.ofNullable(walk.getPhotos())
                .orElse(List.of())
                .stream()
                .map(WalkPhoto::getPhotoUrl)
                .toList();

        WalkResponse response = WalkResponse.fromEntity(walk);
        response.setPoints(points);
        response.setPhotoUrls(photoUrls);

        return response;
    }
}
