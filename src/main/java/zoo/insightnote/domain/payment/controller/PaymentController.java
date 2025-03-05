package zoo.insightnote.domain.payment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import zoo.insightnote.domain.payment.dto.request.PaymentRequestDto;

public interface PaymentController {
    @PostMapping("/request")
    void requestPayment(@RequestBody PaymentRequestDto request);
}
