package zoo.insightnote.domain.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final long PAYMENT_TID_EXPIRATION = 10 * 60; // 10분
    private final long PAYMENT_SESSION_KEYS_EXPIRATION = 5 * 60; // 5분
    private final String PAYMENT_TID_KEY = "payment:tid: ";
    private final String PAYMENT_SESSIONS_KEY = "payment:sessions: ";

    public void saveTidKey(Long orderId, String tid) {
        String tidKey = PAYMENT_TID_KEY + orderId;
        redisTemplate.opsForValue().set(tidKey, tid, PAYMENT_TID_EXPIRATION, TimeUnit.SECONDS);
    }

    public String getTidKey(Long orderId) {
        String tidKey = PAYMENT_TID_KEY + orderId;

        String tid = redisTemplate.opsForValue().get(tidKey);
        if (tid == null) {
            log.error("❌ Redis에서 tid 조회 실패! (orderId={})",  orderId);
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        return tid;
    }

    public void saveSessionIds(Long orderId, List<Long> sessionsId) {
        String sessionIdsKey = PAYMENT_SESSIONS_KEY + orderId;
        try {
            String jsonSessionIds = objectMapper.writeValueAsString(sessionsId);
            redisTemplate.opsForValue().set(sessionIdsKey, jsonSessionIds, PAYMENT_SESSION_KEYS_EXPIRATION, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("❌ JSON 변환 오류 (sessionIds 저장 실패)", e);
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    public String getSessionIds(Long orderId) {
        String sessionIdsKey = PAYMENT_SESSIONS_KEY + orderId;
        String sessionIds = redisTemplate.opsForValue().get(sessionIdsKey);

        if (sessionIds == null) {
            log.error("❌ Redis에서 sessionsId 조회 실패! (orderId={})", orderId);
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        return sessionIds;
    }

}
