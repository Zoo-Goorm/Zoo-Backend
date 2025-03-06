package zoo.insightnote.domain.payment.dto.etc;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

public class AmountDto {
    private int totalAmount;

    private int taxFreeAmount;

    private int vatAmount;

    private int discountAmount;
}
