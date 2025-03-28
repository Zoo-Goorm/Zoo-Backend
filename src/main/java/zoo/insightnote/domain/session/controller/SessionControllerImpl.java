package zoo.insightnote.domain.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.session.dto.SessionResponseDto;
import zoo.insightnote.domain.session.dto.request.SessionCreateRequest;
import zoo.insightnote.domain.session.dto.request.SessionUpdateRequest;
import zoo.insightnote.domain.session.dto.response.*;
import zoo.insightnote.domain.session.dto.response.query.SessionDetailResponse;
import zoo.insightnote.domain.session.dto.response.query.SessionDetaileWithImageAndCountResponse;
import zoo.insightnote.domain.session.dto.response.query.SessionTimeWithAllListGenericResponse;
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
//    @Override
//    @GetMapping("/sessions")
//    public ResponseEntity<SessionAllListwithTimeResponse> getAllSessions() {
//        Map<String, List<SessionResponseDto.SessionAllRes>> response = sessionService.getAllSessions();
//        return ResponseEntity.ok(response);
//    }

//    @Override
//    @GetMapping("/sessions")
//    public ResponseEntity<SessionTimeWithAllListResponse> getAllSessions() {
//        SessionTimeWithAllListResponse response = sessionService.getAllSessions();
//        return ResponseEntity.ok(response);
//    }
//
//    // 2. 세션 전체 조회 (연사 이미지, 인원수 포함)
//    @Override
//    @GetMapping("/sessions/detailed")
//    public ResponseEntity<Map<String, List<SessionResponseDto.SessionDetailedRes>>> getAllSessionsWithDetails() {
//        Map<String, List<SessionResponseDto.SessionDetailedRes>> response = sessionService.getAllSessionsWithDetails();
//        return ResponseEntity.ok(response);
//    }


    @GetMapping("/sessions")
    public ResponseEntity<SessionTimeWithAllListGenericResponse<SessionDetailResponse>> getAllSessions() {
        SessionTimeWithAllListGenericResponse<SessionDetailResponse> response = sessionService.getAllSessions();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sessions/detailed")
    public ResponseEntity<SessionTimeWithAllListGenericResponse<SessionDetaileWithImageAndCountResponse>> getAllSessionsWithDetails() {
        SessionTimeWithAllListGenericResponse<SessionDetaileWithImageAndCountResponse> response = sessionService.getAllSessionsWithDetails();
        return ResponseEntity.ok(response);
    }


    // 세션 단일 상세 조회
    @Override
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<SessionResponseDto.SessionSpeakerDetailRes> getSessionDetails(@PathVariable Long sessionId) {
        SessionResponseDto.SessionSpeakerDetailRes response = sessionService.getSessionDetails(sessionId);
        return ResponseEntity.ok(response);
    }


}
