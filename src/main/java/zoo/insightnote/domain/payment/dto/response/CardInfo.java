package zoo.insightnote.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CardInfo(
        @JsonProperty("kakaopay_purchase_corp") String kakaopay_purchase_corp,
        @JsonProperty("kakaopay_purchase_corp_code") String kakaopay_purchase_corp_code,
        @JsonProperty("kakaopay_issuer_corp") String kakaopay_issuer_corp,
        @JsonProperty("kakaopay_issuer_corp_code") String kakaopay_issuer_corp_code,
        @JsonProperty("bin") String bin,
        @JsonProperty("card_type") String card_type,
        @JsonProperty("install_month") String install_month,
        @JsonProperty("approved_id") String approved_id,
        @JsonProperty("card_mid") String card_mid,
        @JsonProperty("interest_free_install") String interest_free_install,
        @JsonProperty("installment_type") String installment_type,
        @JsonProperty("card_item_code") String card_item_code
) {}

