package zoo.insightnote.domain.payment.dto.response;

public record ApproveCancelAmount (
    int total,
    int tax_free,
    int vat,
    int point,
    int discount,
    int green_deposit
){ }
