package zoo.insightnote.domain.payment.dto.request;

public record PaymentApproveRequest (
    Long orderId,
    Long userId,
    String pgToken,
    String username
) {}
