package zoo.insightnote.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Value("${FRONT_URL}")
    private String frontUrl;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://www.synapsex.online") // ✅ 명확한 도메인 지정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // ✅ 명확한 메서드 지정
                .allowedHeaders("*")
                .allowCredentials(true); // ✅ 쿠키 허용
    }
}

