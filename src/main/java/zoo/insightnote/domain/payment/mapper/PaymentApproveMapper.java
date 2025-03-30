package zoo.insightnote.domain.payment.mapper;

import zoo.insightnote.domain.payment.dto.request.PaymentApproveRequest;

public class PaymentApproveMapper {
    public static PaymentApproveRequest toBuildPaymentApprove(
            Long orderId,
            Long userId,
            String pgToken,
            String username
    ) {
        return new PaymentApproveRequest(orderId, userId, pgToken, username);
    }
}
