package org.example.stamppaw_backend.random.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class LocationUpdateResponse {
    private boolean speedValid;          // 속도 정상인지 (자동차/자전거 의심 여부)
    private boolean generatedNewPoints;  // 새 랜덤 포인트 생성 여부
    private int visitedPointCount;       // 이번 요청에서 새로 방문 처리된 개수
    private List<RandomPointDto> points;
    private int reward;
}
