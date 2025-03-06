package zoo.insightnote.domain.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.session.dto.SessionRequest;
import zoo.insightnote.domain.session.dto.SessionResponse;
import zoo.insightnote.domain.session.service.SessionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sessions")
public class SessionControllerImpl implements SessionController{
    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SessionResponse.Default> createSession(
            @RequestBody SessionRequest.Create request
    ) {
        SessionResponse.Default response = sessionService.createSession(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<SessionResponse.Default> updateSession(
            @PathVariable Long sessionId,
            @RequestBody SessionRequest.Update request) {
        SessionResponse.Default response = sessionService.updateSession(sessionId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) {
        sessionService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionResponse.Default> getSessionById(@PathVariable Long sessionId) {
        SessionResponse.Default response = sessionService.getSessionById(sessionId);
        return ResponseEntity.ok(response);
    }


}
