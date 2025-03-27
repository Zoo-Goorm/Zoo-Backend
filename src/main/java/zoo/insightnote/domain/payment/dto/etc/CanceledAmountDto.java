package zoo.insightnote.domain.payment.dto.etc;

import lombok.Getter;

@Getter
public class CanceledAmountDto {
    private int total;
    private int tax_free;
    private int vat;
    private int point;
    private int discount;
    private int green_deposit;
}