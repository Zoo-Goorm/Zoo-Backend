package zoo.insightnote.domain.payment.mapper;

import zoo.insightnote.domain.payment.dto.request.PaymentCancelRequest;

public class PaymentCancelMapper {
    public static PaymentCancelRequest toBuildPaymentCancel (
            String tid,
            int cancelAmount,
            int cancelTaxFreeAmount
    ) {
        return new PaymentCancelRequest(tid, cancelAmount, cancelTaxFreeAmount);
    }
}