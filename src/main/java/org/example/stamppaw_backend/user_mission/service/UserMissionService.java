package org.example.stamppaw_backend.user_mission.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.admin.mission.repository.MissionRepository;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.user_mission.dto.UserMissionDto;
import org.example.stamppaw_backend.user_mission.entity.UserMission;
import org.example.stamppaw_backend.user_mission.repository.UserMissionRepository;
import org.example.stamppaw_backend.user.entity.User;
import org.example.stamppaw_backend.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserMissionService {

    private final UserMissionRepository userMissionRepository;
    private final MissionRepository missionRepository;
    private final UserService userService;

    public UserMission createUserMission(Long userId, Long missionId) {
        User user = userService.getUserOrException(userId);
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new StampPawException(ErrorCode.MISSION_NOT_FOUND));

        UserMission userMission = UserMission.builder()
                .user(user)
                .mission(mission)
                .startDate(LocalDate.now())
                .status(false)
                .build();

        return userMissionRepository.save(userMission);
    }

    @Transactional(readOnly = true)
    public List<UserMissionDto> getUserMissions(Long userId) {
        return userMissionRepository.findByUserId(userId)
                .stream()
                .map(UserMissionDto::fromEntity)
                .toList();
    }

    @Transactional
    // 관리자가 직접 미션 완료
    public UserMissionDto completeMission(Long userMissionId) {
        UserMission userMission = userMissionRepository.findById(userMissionId)
                .orElseThrow(() -> new StampPawException(ErrorCode.MISSION_NOT_FOUND));

        if (userMission.isStatus()) {
            throw new StampPawException(ErrorCode.MISSION_ALREADY_COMPLETED);
        }

        userMission.setStatus(true);
        userMissionRepository.save(userMission);

        return UserMissionDto.fromEntity(userMission);
    }
}
