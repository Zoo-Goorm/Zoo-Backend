package zoo.insightnote.domain.payment.dto.response;

public record KakaoPayCancelResponse (
     String aid,
     String tid,
     String cid,
     String status,
     String partner_order_id,
     String partner_user_id,
     String payment_method_type,
     Amount amount,
     ApproveCancelAmount approved_cancel_amount,
     CanceledAmount canceled_amount,
     CancelAvailableAmount cancel_available_amount,
     String item_name,
     String item_code,
     int quantity,
     String created_at,
     String approve_at,
     String canceled_at,
     String payload
) { }
