package org.example.stamppaw_backend.point.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.example.stamppaw_backend.user_mission.entity.UserMission;
import org.example.stamppaw_backend.point.dto.PointRequest;
import org.example.stamppaw_backend.point.dto.PointResponse;
import org.example.stamppaw_backend.point.entity.Point;
import org.example.stamppaw_backend.point.repository.PointRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

    private final PointRepository pointRepository;
    private final UserService userService;

    // 포인트 지급
    public PointResponse addPoint(Long userId, PointRequest request) {
        User user = userService.getUserOrException(userId);

        int amount = request.getAmount();
        MissionType reason = request.getReason();

        user.setTotalPoint(user.getTotalPoint() + amount);

        Point point = Point.builder()
                .amount(amount)
                .reason(reason)
                .user(user)
                .userMission(null) // 미션 없음
                .build();

        Point saved = pointRepository.save(point);

        return new PointResponse(saved.getId(), saved.getAmount(), saved.getReason());
    }

    @Transactional(readOnly = true)
    public List<PointResponse> getPoints(Long userId) {
        User user = userService.getUserOrException(userId);

        return pointRepository.findAllByUser(user)
                .stream()
                .map(p -> new PointResponse(p.getId(), p.getAmount(), p.getReason()))
                .toList();
    }

    @Transactional(readOnly = true)
    public int getTotalPointsForUser(Long userId) {
        User user = userService.getUserOrException(userId);

        return (int) user.getTotalPoint();
    }

    public void deletePoint(Long pointId) {
        Point point = pointRepository.findById(pointId)
                .orElseThrow(() -> new StampPawException(ErrorCode.POINT_NOT_FOUND));

        User user = point.getUser();
        user.setTotalPoint(user.getTotalPoint() - point.getAmount());

        pointRepository.delete(point);
    }
}
