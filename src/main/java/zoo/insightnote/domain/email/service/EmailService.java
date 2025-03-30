package zoo.insightnote.domain.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.service.EventService;
import zoo.insightnote.domain.payment.entity.Payment;
import zoo.insightnote.domain.payment.repository.PaymentRepository;
import zoo.insightnote.domain.reservation.dto.response.UserTicketInfoResponseDto;
import zoo.insightnote.domain.reservation.repository.ReservationCustomQueryRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final ReservationCustomQueryRepository reservationCustomQueryRepository;
    private final EventService eventService;
    private final PaymentRepository paymentRepository;

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

    public void sendPaymentSuccess(User user) throws MessagingException {
        // 세션 내역(세션 이름, 시간대, 연사 이름), 결제 내역(선택 날짜, 금액), 참가자 정보(전화번호, 이름, 이메일)
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        UserTicketInfoResponseDto ticketInfo = reservationCustomQueryRepository.processUserTicketInfo(user.getUsername());

        // 세션 내역 구성
        StringBuilder sessionList = new StringBuilder();
        Set<String> selectedDates = new HashSet<>();

        ticketInfo.getRegisteredSessions().forEach((date, sessions) -> {
            selectedDates.add(date); // 날짜 수집
            for (UserTicketInfoResponseDto.reservationSessions session : sessions) {
                sessionList.append("<li>")
                        .append("<strong>").append(session.getSessionName()).append("</strong><br>")
                        .append("- 시간대: ").append(session.getTimeRange()).append("<br>")
                        .append("- 연사: ").append(session.getSpeakerName()).append("</li><br>");
            }
        });

        // 입장권 구분
        String selectDate = "";
        if (ticketInfo.getTickets().size() >= 2) {
            List<Map.Entry<String, Boolean>> ticketList = new ArrayList<>(ticketInfo.getTickets().entrySet());

            boolean firstDay = ticketList.get(0).getValue();
            boolean secondDay = ticketList.get(1).getValue();

            if (firstDay && secondDay) {
                selectDate = "양일(1일차/2일차)";
            } else if (firstDay) {
                selectDate = "1일차";
            } else if (secondDay) {
                selectDate = "2일차";
            }
        }

        // 입장권 가격
        Event event = eventService.findById(ticketInfo.getEventId());
        Payment reservedEvent = paymentRepository.findReservedEvent(user.getUsername(), event.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.EVENT_NOT_FOUND));
        int amount = reservedEvent.getAmount();

        // 이름 가운데 * 처리
        String name = user.getName();
        String maskedName = name.length() >= 3
                ? name.charAt(0) + "*".repeat(name.length() - 2) + name.charAt(name.length() - 1)
                : name;

        // 전화번호 * 처리
        String phone = user.getPhoneNumber().replaceAll("\\D", "");
        String maskedPhone = phone.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-****");

        // 현재 시간
        String paymentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy / MM / dd   HH:mm:ss"));

        String htmlMsg = "<html>" +
                "<body style=\"font-family: 'Apple SD Gothic Neo', sans-serif; line-height: 1.6;\">" +

                "<h2>[Synapse X] 결제 및 참가 신청이 정상적으로 완료되었습니다.</h2>" +

                "<p><strong>" + maskedName + "</strong>님, 참가 신청을 해주셔서 감사합니다.</p>" +

                "<p>아래의 결제 내역을 확인해주세요.<br>" +
                "자세한 세션 정보는 " +
                "<a href=\"https://www.synapsex.online/mypage/sessions\" target=\"_blank\" style=\"color: #4a90e2;\">" +
                "‘마이페이지’ > ‘내 세션 리스트’</a>에서 확인하실 수 있습니다.</p>" +

                "<hr style=\"margin: 20px 0;\" />" +

                "<h3>[선택한 세션 내역]</h3>" +
                "<ul style=\"list-style-type: none; padding-left: 0;\">" +
                sessionList.toString() +
                "</ul>" +

                "<h3>[결제 내역]</h3>" +
                "<ul style=\"list-style-type: none; padding-left: 0;\">" +
                "<li><strong>선택 날짜:</strong> " + selectDate + "</li>" +
                "<li><strong>결제 수단:</strong> 카카오페이</li>" +
                "<li><strong>결제 금액:</strong> " + String.format("%,d원", amount) + "</li>" +
                "</ul>" +

                "<h3>[결제 일시]</h3>" +
                "<p>" + paymentTime + "</p>" +

                "<h3>[결제자 정보]</h3>" +
                "<ul style=\"list-style-type: none; padding-left: 0;\">" +
                "<li><strong>이름:</strong> " + maskedName + "</li>" +
                "<li><strong>전화번호:</strong> " + maskedPhone + "</li>" +
                "<li><strong>이메일:</strong> " + user.getEmail() + "</li>" +
                "</ul>" +

                "<h3>[개인정보 이용약관]</h3>" +
                "<p>본인은 개인정보보호법 제15조 및 제17조, 제22조, 제29조, 제31조, 신용 정보의 이용 및 보호에 관한 법률 제33조에 따라 " +
                "SYNXCON 2025 운영사무국에서 아래의 내용과 같이 본인의 개인(신용) 정보를 수집·이용하는데 동의합니다.</p>" +

                "<p><strong>※ 개인(신용) 정보의 수집·이용에 관한 사항</strong></p>" +
                "<p>SYNXCON 2025 운영사무국은 개인 정보를 중요시하며, 정보통신망 이용 촉진 및 정보보호 등에 관한 법률을 준수하고 있습니다.<br>" +
                "SYNXCON 2025와 관련된 개인 정보를 수집, 이용목적 이외에 다른 용도로 이를 이용하거나 이용자의 동의 없이 제 3자에게 이를 제공하지 않습니다.</p>" +

                "<p><strong>※ 수집하는 개인 정보 항목</strong><br>" +
                "- 성명, 전화번호, 이메일 주소, 직군/직업, 가입경로</p>" +

                "<p><strong>※ 개인 정보의 수집 목적</strong><br>" +
                "- 수집 목적: 서비스 이용을 위한 본인 식별, 등록 등의 정보 관리, 포럼 관련 정보 전달 및 연락</p>" +

                "<p><strong>※ 개인 정보의 보유/이용 기간 및 폐기</strong><br>" +
                "- 수집된 개인 정보는 정보 등록일로부터 보유 및 이용하게 됩니다.<br>" +
                "- 행사 종료일로부터 회원은 3년, 비회원은 6개월입니다.<br>" +
                "- 개인 정보를 파기할 때에는 아래와 같이 재생할 수 없는 방법을 사용하여 이를 삭제합니다.<br>" +
                "- 기록물, 인쇄물, 서면 그 밖의 기록 매체인 경우 파쇄 또는 소각합니다.<br>" +
                "- 전자적 파일 형태인 경우 복원이 불가능한 방법으로 영구 삭제합니다.</p>" +

                "<p><strong>※ 개인 정보의 제공</strong><br>" +
                "SYNXCON 2025는 원활한 행사 정보 전달 및 등록 서비스를 위해 개인정보의 처리를 아래와 같이 업무 위탁하고 있습니다.<br>" +
                "관계법령에 따라 위탁 계약 시 개인정보가 안전하게 관리될 수 있도록 필요한 조치를 하고 있으며, 수탁자의 개인정보 보호조치 능력을 고려하고, " +
                "개인정보의 안전한 관리 및 파기 등 수탁자의 의무 이행 여부를 주기적으로 확인합니다.<br>" +
                "- (주)시냅스엑스 : SYNXCON 2025 운영사무국 및 행사 운영사, 등록정보, 행사 안내 등 이메일, 문자 컨텐츠 발송, 홈페이지/온라인프로그램 관리, 개인정보가 저장된 서버 운영 및 관리, 등록정보</p>" +

                "<p><strong>※ 정보주체의 권리와 의무</strong><br>" +
                "- 정보주체는 언제든지 이용자의 개인정보를 열람, 정정할 수 있으며, 자신의 개인정보 제공에 관한 동의철회/알림 수신 해지를 요청할 수 있습니다.<br>" +
                "동시에 자신의 개인정보를 보호할 의무가 있으며, 본인의 부주의(로그인 상태에서 이석, 접근매체 양도 및 대여 등)나 관계법령에 의한 보안조치로 차단할 수 없는 방법이나 " +
                "기술을 사용한 해킹 등 SYNXCON 2025 운영사무국이 상당한 주의에도 불구하고 통제할 수 없는 문제 등으로 개인정보가 유출되어 발생한 문제에 대해서 책임지지 않습니다.<br>" +
                "- 정보주체는 자신의 개인 정보를 보호할 의무가 있으며, 자신의 개인 정보를 최신의 상태로 유지해야 하며, 이용자의 부정확한 정보 입력으로 발생하는 문제의 책임은 정보주체에게 있습니다.</p>" +

                "<p><strong>※ 개인 정보의 안전한 관리</strong><br>" +
                "- SYNXCON 2025 준비사무국은 개인 정보를 취급함에 있어 개인 정보가 분실, 도난, 누출, 변조 또는 훼손되지 않도록 안전성 확보를 위해 다음과 같은 기술적/관리적 보호대책을 강구하고 있습니다.<br>" +
                "- 개인 정보는 완전하게 암호화되어 저장, 관리합니다.<br>" +
                "- 개인 정보 취급자를 최소한으로 제한하며, 개인 정보 취급자에 대한 교육 등 관리적 조치를 통해 개인 정보보호의 중요성을 항상 강조하고 있습니다.</p>" +

                "<p><strong>본 메일은 발신 전용으로 회신되지 않습니다.</strong><br>" +
                "<strong>문의사항은 Synapse X 이메일(synapsex@sample.com)로 보내주세요.</strong><br>" +
                "<strong>결제 관련 정보, 주요 공지사항 및 이벤트 당첨 안내 등은 수신 동의 여부에 관계없이 발송됩니다.</strong></p>" +

                "</body></html>";

        helper.setTo(user.getEmail());
        helper.setSubject("[Synapse X] 결제 완료 안내");
        helper.setText(htmlMsg, true);
        mailSender.send(mimeMessage);
    }
}
