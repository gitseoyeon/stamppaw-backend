package org.example.stamppaw_backend.admin.mission.service;

import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.admin.mission.entity.Mission;
import org.example.stamppaw_backend.admin.mission.repository.MissionRepository;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.example.stamppaw_backend.admin.mission.dto.MissionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionService {

    private final MissionRepository missionRepository;

    public MissionDto createMission(MissionDto dto) {
        Mission mission = Mission.builder()
                .content(dto.getContent())
                .point(dto.getPoint())
                .type(dto.getType())
                .build();

        return toDto(missionRepository.save(mission));
    }

    @Transactional(readOnly = true)
    public List<MissionDto> getAllMissions() {
        return missionRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public MissionDto getMissionById(Long id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new StampPawException(ErrorCode.MISSION_NOT_FOUND));
        return toDto(mission);
    }

    public MissionDto updateMission(Long id, MissionDto dto) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new StampPawException(ErrorCode.MISSION_NOT_FOUND));

        mission.setContent(dto.getContent());
        mission.setPoint(dto.getPoint());
        mission.setType(dto.getType());

        return toDto(missionRepository.save(mission));
    }

    public void deleteMission(Long id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new StampPawException(ErrorCode.MISSION_NOT_FOUND));
        missionRepository.delete(mission);
    }

    private MissionDto toDto(Mission mission) {
        return MissionDto.builder()
                .id(mission.getId())
                .content(mission.getContent())
                .point(mission.getPoint())
                .type(mission.getType())
                .build();
    }
}