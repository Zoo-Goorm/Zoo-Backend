package zoo.insightnote.domain.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.session.dto.SessionRequestDto;
import zoo.insightnote.domain.session.dto.SessionResponseDto;
import zoo.insightnote.domain.session.service.SessionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sessions")
public class SessionControllerImpl implements SessionController {
    private final SessionService sessionService;

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

    @Override
    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionResponseDto.SessionRes> getSessionById(@PathVariable Long sessionId) {
        SessionResponseDto.SessionRes response = sessionService.getSessionById(sessionId);
        return ResponseEntity.ok(response);
    }

}
