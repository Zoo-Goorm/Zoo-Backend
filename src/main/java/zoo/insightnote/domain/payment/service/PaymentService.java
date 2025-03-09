package zoo.insightnote.domain.payment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.repository.EventRepository;
import zoo.insightnote.domain.payment.dto.request.PaymentApproveRequestDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayApproveResponseDto;
import zoo.insightnote.domain.payment.entity.Payment;
import zoo.insightnote.domain.payment.entity.PaymentStatus;
import zoo.insightnote.domain.payment.repository.PaymentRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final KakaoPayService kakaoPayService;
    private final PaymentRepository paymentRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    public ResponseEntity<KakaoPayApproveResponseDto> approvePayment(PaymentApproveRequestDto requestDto) {
        log.info("✅ 결제 승인 요청 도착! orderId: {}, userId: {}, pg_token: {}", requestDto.getOrderId(), requestDto.getUserId(), requestDto.getPgToken());

        // ✅ Redis에서 tid 조회
        String tidKey = "payment: " + requestDto.getOrderId();
        String tid = redisTemplate.opsForValue().get(tidKey);
        if (tid == null) {
            log.error("❌ Redis에서 tid 조회 실패! (orderId={})",  requestDto.getOrderId());
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        String getSessionIds = kakaoPayService.getSessionIds(requestDto.getOrderId());
        if (getSessionIds == null) {
            log.error("❌ Redis에서 sessions 조회 실패! (orderId={})",  requestDto.getOrderId());
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> sessionIds = objectMapper.readValue(getSessionIds, new TypeReference<List<Long>>() {});

        return response;
    }

    private void savePaymentInfo(KakaoPayApproveResponseDto responseDto) {
        User userInfo = findUserById(Long.valueOf(responseDto.getPartner_user_id()));
        Event eventInfo = findEventById(Long.valueOf(100));

        log.info("🔍 [결제 저장 직전] amount: {}", responseDto.getAmount().getTotalAmount());

        Payment payment = Payment.builder()
                .user(userInfo)
                .event(eventInfo)
                .email(userInfo.getEmail())
                .phoneNumber(userInfo.getPhoneNumber())
                .amount(responseDto.getAmount().getTotalAmount())
                .paymentStatus(PaymentStatus.COMPLETED)
                .build();

        paymentRepository.save(payment);
        log.info("✅ 결제 정보 저장 완료: {}", payment);
    }

    // TODO : 유저 도메인 개발 완료시 삭제
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(null, "User 사용자를 찾을 수 없습니다."));
    }

    // TODO : 유저 도메인 개발 완료시 삭제
    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomException(null, "event 사용자를 찾을 수 없습니다."));
    }
}
