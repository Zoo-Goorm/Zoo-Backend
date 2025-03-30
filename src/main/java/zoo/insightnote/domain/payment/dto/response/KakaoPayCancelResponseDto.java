package zoo.insightnote.domain.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zoo.insightnote.domain.payment.dto.etc.ApproveCancelAmountDto;
import zoo.insightnote.domain.payment.dto.etc.CancelAvailableAmountDto;
import zoo.insightnote.domain.payment.dto.etc.CanceledAmountDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayCancelResponseDto {
    private String aid;
    private String tid;
    private String cid;
    private String status;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private Amount amount;
    private ApproveCancelAmountDto approved_cancel_amount;
    private CanceledAmountDto canceled_amount;
    private CancelAvailableAmountDto cancel_available_amount;
    private String item_name;
    private String item_code;
    private int quantity;
    private String created_at;
    private String approve_at;
    private String canceled_at;
    private String payload;
}
