package zoo.insightnote.domain.payment.dto.response;

import java.time.LocalDateTime;

public record KakaoPayApproveResponse (
        String aid,
        String tid,
        String cid,
        String sid,
        String partner_order_id,
        String partner_user_id,
        String payment_method_type,
        Amount amount,
        String item_name,
        LocalDateTime created_at,
        LocalDateTime approved_at,
        CardInfo card_info
) { }
