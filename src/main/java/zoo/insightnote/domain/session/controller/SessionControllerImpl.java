package zoo.insightnote.domain.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.session.dto.request.SessionCreateRequest;
import zoo.insightnote.domain.session.dto.request.SessionUpdateRequest;
import zoo.insightnote.domain.session.dto.response.*;
import zoo.insightnote.domain.session.dto.response.SessionDetailResponse;
import zoo.insightnote.domain.session.dto.response.SessionDetaileWithImageAndCountResponse;
import zoo.insightnote.domain.session.dto.response.SessionTimeWithAllListGenericResponse;
import zoo.insightnote.domain.session.dto.response.SessionWithSpeakerDetailResponse;
import zoo.insightnote.domain.session.service.SessionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SessionControllerImpl implements SessionController {
    private final SessionService sessionService;

    @Override
    @PostMapping("/sessions")
    public ResponseEntity<SessionCreateResponse> createSession(
            @RequestBody SessionCreateRequest request
    ) {
        SessionCreateResponse response = sessionService.createSession(request);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/sessions/{sessionId}")
    public ResponseEntity<SessionUpdateResponse> updateSession(
            @PathVariable Long sessionId,
            @RequestBody SessionUpdateRequest request) {
       SessionUpdateResponse response = sessionService.updateSession(sessionId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) {
        sessionService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }


    // 1. 세션 전체 조회 (이미지 제외, 인원수 제외)
    @GetMapping("/sessions")
    public ResponseEntity<SessionTimeWithAllListGenericResponse<SessionDetailResponse>> getAllSessions() {
        SessionTimeWithAllListGenericResponse<SessionDetailResponse> response = sessionService.getAllSessions();
        return ResponseEntity.ok(response);
    }

    // 2. 세션 전체 조회 (이미지 , 인원수 포함)
    @GetMapping("/sessions/detailed")
    public ResponseEntity<SessionTimeWithAllListGenericResponse<SessionDetaileWithImageAndCountResponse>> getAllSessionsWithDetails() {
        SessionTimeWithAllListGenericResponse<SessionDetaileWithImageAndCountResponse> response = sessionService.getAllSessionsWithDetails();
        return ResponseEntity.ok(response);
    }


    // 세션 단일 상세 조회
    @Override
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<SessionWithSpeakerDetailResponse> getSessionDetails(@PathVariable Long sessionId) {
        SessionWithSpeakerDetailResponse response = sessionService.getSessionDetails(sessionId);
        return ResponseEntity.ok(response);
    }


}
