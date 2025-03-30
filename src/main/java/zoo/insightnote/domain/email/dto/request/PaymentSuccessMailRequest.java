package zoo.insightnote.domain.email.dto.request;

public record PaymentSuccessMailRequest(
        String userNameMasked,
        String userPhoneMasked,
        String userEmail,
        String paymentTime,
        String selectedDateLabel,
        int amount,
        String sessionHtml
) { }
