package zoo.insightnote.domain.payment.dto.etc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AmountDto {
    @JsonProperty("total")
    private int totalAmount;  // 전체 결제 금액

    @JsonProperty("tax_free")
    private int taxFreeAmount;  // 비과세 금액

    @JsonProperty("vat")
    private int vatAmount;  // 부가세 금액

    @JsonProperty("point")
    private int pointAmount;  // 사용한 포인트 금액

    @JsonProperty("discount")
    private int discountAmount;  // 할인 금액

    @JsonProperty("green_deposit")
    private int greenDepositAmount;  // 컵 보증금
}
