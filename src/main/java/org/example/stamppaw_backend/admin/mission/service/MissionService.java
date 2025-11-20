package org.example.stamppaw_backend.admin.mission.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.admin.mission.dto.MissionRequest;
import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.admin.mission.repository.MissionRepository;
import org.example.stamppaw_backend.admin.mission.service.creator.MissionCreator;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.admin.mission.dto.MissionDto;
import org.example.stamppaw_backend.user_mission.entity.MissionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionService {

    private final MissionRepository missionRepository;
    private final List<MissionCreator<?>> creators;

    private final Map<MissionType, MissionCreator<?>> creatorMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (MissionCreator<?> creator : creators) {
            creatorMap.put(creator.supports(), creator);
        }
    }

    public MissionDto createMission(MissionRequest req) {

        MissionType type = req.getType();

        if (missionRepository.existsByType(type)) {
            throw new StampPawException(ErrorCode.MISSION_ALREADY_EXISTS);
        }

        MissionCreator creator = creatorMap.get(type);
        if (creator == null) {
            throw new StampPawException(ErrorCode.INVALID_MISSION_TYPE);
        }

        Mission mission = creator.create(req);
        missionRepository.save(mission);

        return MissionDto.fromEntity(mission);
    }

    public List<MissionDto> getAllMissions() {
        return missionRepository.findAll().stream()
                .map(MissionDto::fromEntity)
                .toList();
    }

    public MissionDto getMissionById(Long id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new StampPawException(ErrorCode.MISSION_NOT_FOUND));
        return MissionDto.fromEntity(mission);
    }

    public List<MissionType> getUsedMissionTypes() {
        return missionRepository.findDistinctTypes();
    }

    @Transactional
    public MissionDto updateMission(Long id, MissionRequest req) {

        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new StampPawException(ErrorCode.MISSION_NOT_FOUND));

        if (mission.getType() != req.getType()) {
            throw new StampPawException(ErrorCode.INVALID_MISSION_TYPE_CHANGE);
        }

        MissionCreator creator = creatorMap.get(req.getType());
        if (creator == null) {
            throw new StampPawException(ErrorCode.INVALID_MISSION_TYPE);
        }

        creator.update(mission, req);

        missionRepository.save(mission);

        return MissionDto.fromEntity(mission);
    }


//    public void deleteMission(Long id) {
//        Mission mission = missionRepository.findById(id)
//                .orElseThrow(() -> new StampPawException(ErrorCode.MISSION_NOT_FOUND));
//        missionRepository.delete(mission);
//    }
}
