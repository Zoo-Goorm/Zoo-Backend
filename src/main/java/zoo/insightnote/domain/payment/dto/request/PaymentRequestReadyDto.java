package zoo.insightnote.domain.payment.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import zoo.insightnote.domain.payment.dto.etc.UserInfoDto;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PaymentRequestReadyDto {
    @NotBlank
    private String itemName;

    @Positive()
    private int totalAmount;

    @Positive()
    private int quantity;

    @NotEmpty
    private List<Long> sessionIds;

    @NotNull
    @Valid
    private UserInfoDto userInfo;
}