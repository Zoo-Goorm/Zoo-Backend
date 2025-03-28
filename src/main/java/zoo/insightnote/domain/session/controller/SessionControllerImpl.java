package zoo.insightnote.domain.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.service.InsightService;
import zoo.insightnote.domain.session.dto.SessionRequestDto;
import zoo.insightnote.domain.session.dto.SessionResponseDto;
import zoo.insightnote.domain.session.dto.request.SessionCreateRequest;
import zoo.insightnote.domain.session.dto.request.SessionUpdateRequest;
import zoo.insightnote.domain.session.dto.response.SessionCreateResponse;
import zoo.insightnote.domain.session.dto.response.SessionTimeWithAllListResponse;
import zoo.insightnote.domain.session.dto.response.SessionUpdateResponse;
import zoo.insightnote.domain.session.service.SessionService;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.service.UserService;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.List;
import java.util.Map;

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

    @Override
    @GetMapping("/sessions")
    public ResponseEntity<SessionTimeWithAllListResponse> getAllSessions() {
        SessionTimeWithAllListResponse response = sessionService.getAllSessions();
        return ResponseEntity.ok(response);
    }

    // 2. 세션 전체 조회 (연사 이미지, 인원수 포함)
    @Override
    @GetMapping("/sessions/detailed")
    public ResponseEntity<Map<String, List<SessionResponseDto.SessionDetailedRes>>> getAllSessionsWithDetails() {
        Map<String, List<SessionResponseDto.SessionDetailedRes>> response = sessionService.getAllSessionsWithDetails();
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
