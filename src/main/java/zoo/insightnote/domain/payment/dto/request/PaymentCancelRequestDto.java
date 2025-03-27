package zoo.insightnote.domain.payment.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PaymentCancelRequestDto {
    private String tid;
    private int cancelAmount;
    private int cancelTaxFreeAmount;
}
