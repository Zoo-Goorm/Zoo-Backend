package zoo.insightnote.domain.qr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.qr.dto.QrCheckDto;
import zoo.insightnote.domain.qr.service.QrService;

@RestController
@RequestMapping("/api/v1/QR")
@RequiredArgsConstructor
public class QrControllerImpl implements QrController {
    private final QrService qrService;

    @PostMapping("/{qrType}/{Id}")
    public void createQR(
            @PathVariable String qrType,
            @PathVariable Long Id) {
        qrService.createQr(qrType, Id);
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<QrCheckDto> checkSessionQr(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        return qrService.checkSessionQr(sessionId, username);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<QrCheckDto> checkEventQr(
            @PathVariable Long eventId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        String username = userDetails.getUsername();
        return qrService.checkEventQr(eventId, username);
    }
}
