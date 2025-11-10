package org.example.stamppaw_backend.walk.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.walk.dto.request.WalkEndRequest;
import org.example.stamppaw_backend.walk.dto.request.WalkPointRequest;
import org.example.stamppaw_backend.walk.dto.request.WalkRecordRequest;
import org.example.stamppaw_backend.walk.dto.request.WalkStartRequest;
import org.example.stamppaw_backend.walk.dto.response.WalkEndResponse;
import org.example.stamppaw_backend.walk.dto.response.WalkResponse;
import org.example.stamppaw_backend.walk.dto.response.WalkStartResponse;
import org.example.stamppaw_backend.walk.service.WalkMapService;
import org.example.stamppaw_backend.walk.service.WalkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

@RestController
@RequestMapping("/api/walks")
@RequiredArgsConstructor
public class WalkController {

    private final WalkService walkService;
    private final WalkMapService walkMapService;

    @PostMapping("/start")
    public ResponseEntity<WalkStartResponse> startWalk(@RequestBody WalkStartRequest request) {
        WalkStartResponse response = walkService.startWalk(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{walkId}/point")
    public ResponseEntity<Void> addPoints(
            @PathVariable Long walkId,
            @RequestBody WalkPointRequest request
    ) {
        walkMapService.addPoint(walkId, request.getLat(), request.getLng(), request.getTimestamp());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{walkId}/end")
    public ResponseEntity<WalkEndResponse> endWalk(
            @PathVariable Long walkId,
            @RequestBody WalkEndRequest request
    ) {
        WalkEndResponse response = walkService.endWalk(walkId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{walkId}/record")
    public ResponseEntity<WalkResponse> recordWalk(
            @PathVariable Long walkId,
            @ModelAttribute WalkRecordRequest request
    ) {
        try {
            WalkResponse response = walkService.editWalk(walkId, request);
            return ResponseEntity.ok(response);
        } catch (MultipartException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{walkId}")
    public ResponseEntity<WalkResponse> getWalkDetail(@PathVariable Long walkId) {
        WalkResponse response = walkService.getWalkDetail(walkId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<WalkResponse>> getUserWalksPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WalkResponse> walks = walkService.getUserWalks(pageable);
        return ResponseEntity.ok(walks);
    }

    @DeleteMapping("/{walkId}")
    public ResponseEntity<Void> deleteWalk(@PathVariable Long walkId) {
        walkService.deleteWalk(walkId);
        return ResponseEntity.ok().build();
    }
}
