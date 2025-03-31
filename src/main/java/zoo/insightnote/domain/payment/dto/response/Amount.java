package zoo.insightnote.domain.payment.dto.response;

public record Amount(
        int total,  // 전체 결제 금액
        int tax_free,  // 비과세 금액
        int vat,  // 부가세 금액
        int point,  // 사용한 포인트 금액
        int discount,  // 할인 금액
        int green_deposit  // 컵 보증금
) {}
