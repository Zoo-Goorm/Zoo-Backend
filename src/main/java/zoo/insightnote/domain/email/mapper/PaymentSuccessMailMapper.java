package zoo.insightnote.domain.email.mapper;

import zoo.insightnote.domain.email.dto.request.PaymentSuccessMailRequest;
import zoo.insightnote.domain.email.dto.request.SessionSummary;
import zoo.insightnote.domain.payment.entity.Payment;
import zoo.insightnote.domain.reservation.dto.response.UserTicketInfoResponse;
import zoo.insightnote.domain.user.entity.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PaymentSuccessMailMapper {
    public static PaymentSuccessMailRequest toSuccessMailDto (
            User user,
            Payment payment,
            UserTicketInfoResponse ticketInfo
    ) {
        // 이름
        String name = user.getName();
        String maskedName = name.length() >= 3
                ? name.charAt(0) + "*".repeat(name.length() - 2) + name.charAt(name.length() - 1)
                : name;

        // 전화번호
        String phone = user.getPhoneNumber().replaceAll("\\D", "");
        String maskedPhone = phone.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-****");

        // 결제 날짜
        String selectedDate = getSelectedDateLabel(ticketInfo.tickets());

        // 세션 리스트 구성
        List<SessionSummary> sessionSummaries = new ArrayList<>();
        ticketInfo.registeredSessions().forEach((date, sessions) -> {
            sessions.forEach(s -> {
                sessionSummaries.add(new SessionSummary(
                        s.sessionName(),
                        s.timeRange(),
                        s.speakerName()
                ));
            });
        });

        String sessionHtml = buildSessionHtml(sessionSummaries);

        return new PaymentSuccessMailRequest(
                maskedName,
                maskedPhone,
                user.getEmail(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy / MM / dd   HH:mm:ss")),
                selectedDate,
                payment.getAmount(),
                sessionHtml
        );
    }

    private static String getSelectedDateLabel(Map<String, Boolean> tickets) {
        if (tickets.size() < 2) return "";

        List<Map.Entry<String, Boolean>> ticketList = new ArrayList<>(tickets.entrySet());
        boolean firstDay = ticketList.get(0).getValue();
        boolean secondDay = ticketList.get(1).getValue();

        if (firstDay && secondDay) return "양일(1일차/2일차)";
        if (firstDay) return "1일차";
        if (secondDay) return "2일차";
        return "";
    }

    private static String buildSessionHtml(List<SessionSummary> sessions) {
        StringBuilder html = new StringBuilder();
        for (SessionSummary session : sessions) {
            html.append("<li>")
                    .append("<strong>").append(session.sessionName()).append("</strong><br>")
                    .append("- 시간대: ").append(session.timeRange()).append("<br>")
                    .append("- 연사: ").append(session.speakerName()).append("</li><br>");
        }
        html.append("</ul>");
        return html.toString();
    }
}
