package zoo.insightnote.domain.email.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailService emailService;
    private final StringRedisTemplate redisTemplate;

    public void sendVerificationCode(String email) {
        redisTemplate.delete(email);

        String code = String.valueOf((int) (Math.random() * 900000) + 100000);

        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);

        emailService.sendVerificationCode(email, code);
    }

    public boolean verifyCode(String email, String inputCode) {
        String savedCode = redisTemplate.opsForValue().get(email);
        if (savedCode == null) {
            throw new CustomException(ErrorCode.NO_SAVED_CODE);
        }
        return savedCode.equals(inputCode);
    }
}


