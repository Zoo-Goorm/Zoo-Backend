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
import zoo.insightnote.domain.session.service.SessionService;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.service.UserService;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sessions")
public class SessionControllerImpl implements SessionController {
    private final SessionService sessionService;
    private final InsightService insightService;
    private final UserService userService;

    @Override
    @PostMapping
    public ResponseEntity<SessionResponseDto.SessionRes> createSession(
            @RequestBody SessionRequestDto.Create request
    ) {
        SessionResponseDto.SessionRes response = sessionService.createSession(request);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/{sessionId}")
    public ResponseEntity<SessionResponseDto.SessionRes> updateSession(
            @PathVariable Long sessionId,
            @RequestBody SessionRequestDto.Update request) {
        SessionResponseDto.SessionRes response = sessionService.updateSession(sessionId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) {
        sessionService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    // 1. 세션 전체 조회 (이미지 제외, 인원수 제외)
    @Override
    @GetMapping
    public ResponseEntity<Map<String, List<SessionResponseDto.SessionAllRes>>> getAllSessions() {
        Map<String, List<SessionResponseDto.SessionAllRes>> response = sessionService.getAllSessions();
        return ResponseEntity.ok(response);
    }

    // 2. 세션 전체 조회 (연사 이미지, 인원수 포함)
    @Override
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, List<SessionResponseDto.SessionDetailedRes>>> getAllSessionsWithDetails() {
        Map<String, List<SessionResponseDto.SessionDetailedRes>> response = sessionService.getAllSessionsWithDetails();
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionResponseDto.SessionSpeakerDetailRes> getSessionDetails(@PathVariable Long sessionId) {
        SessionResponseDto.SessionSpeakerDetailRes response = sessionService.getSessionDetails(sessionId);
        return ResponseEntity.ok(response);
    }


}
