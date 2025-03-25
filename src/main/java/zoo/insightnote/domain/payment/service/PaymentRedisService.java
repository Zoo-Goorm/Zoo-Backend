package zoo.insightnote.domain.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import zoo.insightnote.domain.payment.dto.etc.UserInfoDto;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentRedisService {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final long PAYMENT_TID_EXPIRATION = 10 * 60; // 10분
    private final long PAYMENT_SESSION_KEYS_EXPIRATION = 5 * 60; // 5분
    private final String PAYMENT_TID = "payment:tid: ";
    private final String PAYMENT_SESSION = "payment:sessions: ";
    private final String PAYMENT_USERINFO = "payment:userinfo: ";

    public void saveTidKey(Long orderId, String tid) {
        String tidKey = PAYMENT_TID + orderId;
        redisTemplate.opsForValue().set(tidKey, tid, PAYMENT_TID_EXPIRATION, TimeUnit.SECONDS);
    }

    public String getTidKey(Long orderId) {
        String tidKey = PAYMENT_TID + orderId;
        String tid = redisTemplate.opsForValue().get(tidKey);
        if (tid == null) {
            log.error("❌ Redis에서 tid 조회 실패! (orderId={})", orderId);
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }
        return tid;
    }

    public void saveSessionIds(Long orderId, List<Long> sessionsId) {
        String sessionIdsKey = PAYMENT_SESSION + orderId;
        try {
            // ✅ JSON으로 변환하여 Redis에 저장
            String jsonSessionIds = objectMapper.writeValueAsString(sessionsId);
            redisTemplate.opsForValue().set(sessionIdsKey, jsonSessionIds, PAYMENT_SESSION_KEYS_EXPIRATION, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("❌ JSON 변환 오류 (sessionIds 저장 실패)", e);
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    public List<Long> getSessionIds(Long orderId) {
        String sessionIdsKey = PAYMENT_SESSION + orderId;
        String getSessionsIds = redisTemplate.opsForValue().get(sessionIdsKey);

        if (getSessionsIds == null) {
            log.error("❌ Redis에서 sessions 조회 실패! (orderId={})", orderId);
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(getSessionsIds, new TypeReference<List<Long>>() {});
        } catch (JsonProcessingException e) {
            log.error("❌ JSON 변환 오류 (sessionIds 조회 실패)", e);
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    public void saveUserInfo(Long orderId, UserInfoDto userInfo) {
        String userInfoKey = PAYMENT_USERINFO + orderId;
        try {
            String jsonUserInfo = objectMapper.writeValueAsString(userInfo);
            redisTemplate.opsForValue().set(userInfoKey, jsonUserInfo, PAYMENT_SESSION_KEYS_EXPIRATION, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("❌ JSON 변환 오류 (sessionIds 저장 실패)", e);
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    public UserInfoDto getUserInfo(Long orderId) {
        String userInfoKey = PAYMENT_USERINFO + orderId;
        String getUserInfo = redisTemplate.opsForValue().get(userInfoKey);

        if (getUserInfo == null) {
            log.error("❌ Redis에서 sessions 조회 실패! (orderId={})", orderId);
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(getUserInfo, UserInfoDto.class);
        } catch (JsonProcessingException e) {
            log.error("❌ JSON 변환 오류 (userInfo 조회 실패)", e);
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }
}
