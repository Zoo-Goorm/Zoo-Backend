package zoo.insightnote.domain.qr.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrService {
    private final String QR_SAVE_PATH_NAME = "/Users/hyunoi/Desktop/";
    private final String EVENT_QR_REDIRECTION_URL = "https://api.synapsex.online/oauth2/authorization/google";

    public void createEventQR(Long eventId) {
        String eventInfo = EVENT_QR_REDIRECTION_URL;
        String fileName = "event_" + eventId.toString() + ".png";

        int width = 500;
        int height = 500;

        try {
            // QR 정보 삽입
            BitMatrix bitMatrix = new QRCodeWriter().encode(eventInfo, BarcodeFormat.QR_CODE, width, height);

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
}
