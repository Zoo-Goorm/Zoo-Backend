package zoo.insightnote.domain.qr.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.service.EventService;
import zoo.insightnote.domain.payment.entity.Payment;
import zoo.insightnote.domain.payment.repository.PaymentRepository;
import zoo.insightnote.domain.qr.dto.QrCheckDto;
import zoo.insightnote.domain.reservation.entity.Reservation;
import zoo.insightnote.domain.reservation.repository.ReservationRepository;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.service.SessionService;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.service.UserService;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrService {
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final UserService userService;
    private final SessionService sessionService;
    private final EventService eventService;

    // TODO: QR 이미지 파일이 저장되는 경로
    // TODO: 어떻게 설정하면 좋을지 고민해봐야...
    private final String QR_SAVE_PATH_NAME = "/Users/hyunoi/Desktop/";

    private final String EVENT_QR_REDIRECTION_URL = "https://www.synapsex.online/api/v1/QR/event/";
    private final String SESSION_QR_REDIRECTION_URL = "https://www.synapsex.online/api/v1/QR/session/";

    public void createQr(String qrType, Long Id) {
        String qrInfo;
        String fileName;

        if (qrType.equals("event")) {
            qrInfo = EVENT_QR_REDIRECTION_URL + Id.toString();
            fileName = "event_" + Id.toString() + ".png";
        } else if (qrType.equals("session")) {
            qrInfo = SESSION_QR_REDIRECTION_URL + Id.toString();
            fileName = "session_" + Id.toString() + ".png";
        } else {
            throw new CustomException(ErrorCode.QR_GENERATION_FAILED);
        }

        int width = 500;
        int height = 500;

        try {
            // QR 정보 삽입
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrInfo, BarcodeFormat.QR_CODE, width, height);

            // QR 생성
            BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            // QR 저장
            File qrFile = new File(QR_SAVE_PATH_NAME + fileName);
            ImageIO.write(qrImage, "png", qrFile);

        } catch (WriterException | IOException e) {
            log.error("QR 코드 생성 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.QR_GENERATION_FAILED);
        }
    }

    @Transactional
    public ResponseEntity<QrCheckDto> checkSessionQr(Long sessionId, String username) {
        List<Long> sessionList = reservationRepository.findSessionIdsByUsername(username);
        if (!sessionList.contains(sessionId)) {
            throw new CustomException(ErrorCode.SESSION_NOT_FOUND);
        }
        Reservation reservedSession = reservationRepository.findReservedSession(username, sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
        reservedSession.update();
        reservationRepository.save(reservedSession);

        User user = userService.findByUsername(username);
        Session session = sessionService.findSessionBySessionId(sessionId);

        QrCheckDto response = QrCheckDto.builder()
                .name(user.getName())
                .InfoName(session.getName())
                .build();

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<QrCheckDto> checkEventQr(Long eventId, String username) {
        Payment reservedEvent = paymentRepository.findReservedEvent(username, eventId)
                .orElseThrow(() -> new CustomException(ErrorCode.EVENT_NOT_FOUND));

        reservedEvent.update();
        paymentRepository.save(reservedEvent);

        User user = userService.findByUsername(username);
        Event event = eventService.findById(eventId);

        QrCheckDto response = QrCheckDto.builder()
                .name(user.getName())
                .InfoName(event.getName())
                .build();

        return ResponseEntity.ok(response);
    }

}
