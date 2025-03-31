package zoo.insightnote.domain.payment.dto.request;

public record PaymentCancelRequest(
    String tid,
    int cancelAmount,
    int cancelTaxFreeAmount
){ }
