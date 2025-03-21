package zoo.insightnote.domain.qr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zoo.insightnote.domain.qr.service.QrService;

@RestController
@RequestMapping("/api/v1/QR")
@RequiredArgsConstructor
public class QrControllerImpl {
    private final QrService qrService;

    @PostMapping("/{qrType}/{Id}")
    public void createQR(
                        @PathVariable String qrType,
                        @PathVariable Long Id) {
        qrService.createQr(qrType, Id);
    }

}
