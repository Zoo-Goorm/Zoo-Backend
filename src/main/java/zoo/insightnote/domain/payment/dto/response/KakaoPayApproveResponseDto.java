package zoo.insightnote.domain.payment.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zoo.insightnote.domain.payment.dto.etc.AmountDto;
import zoo.insightnote.domain.payment.dto.etc.CardInfoDto;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KakaoPayApproveResponseDto {
    private String aid;

    private String tid;

    private String cid;

    private String sid;

    private String partner_order_id;

    private String partner_user_id;

    private String payment_method_type;

    private AmountDto amount;

    private String item_name;

    private LocalDateTime created_at;

    private LocalDateTime approved_at;

    private CardInfoDto card_info;

}
