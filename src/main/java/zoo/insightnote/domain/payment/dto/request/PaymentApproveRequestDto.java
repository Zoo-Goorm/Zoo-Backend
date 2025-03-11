package zoo.insightnote.domain.payment.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PaymentApproveRequestDto {
    private Long orderId;
    private Long userId;
    private String pgToken;
}
