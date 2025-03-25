package zoo.insightnote.domain.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationCode(String email, String code) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlMsg = "<html>" +
                "<body>" +
                "안녕하세요, <strong>Synapse X</strong>입니다.<br><br>" +
                "아래는 요청하신 인증코드입니다.<br>" +
                "본인을 확인하기 위해 다음 코드를 입력해 주세요.<br><br>" +
                "<strong><u>인증코드</u> : " + code + "</strong><br><br>" +
                "해당 인증코드는 <strong>5분간 유효</strong>합니다.<br>" +
                "본인이 아닌 분이 해당 이메일을 받았다면, 인증코드를 사용하지 마시고 즉시 삭제해 주세요.<br>" +
                "감사합니다.<br><br><br>" +
                "<strong>Synapse X</strong> 드림" +
                "</body>" +
                "</html>";

        helper.setTo(email);
        helper.setSubject("[Synapse X] 인증코드 안내");
        helper.setText(htmlMsg, true);
        mailSender.send(mimeMessage);
    }
}
