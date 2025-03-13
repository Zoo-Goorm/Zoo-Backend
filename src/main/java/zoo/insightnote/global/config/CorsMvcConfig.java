package zoo.insightnote.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Value("${FRONT_URL}")
    private String frontUrl;

    // 배포된 프론트엔드 주소 추가해야됨
    private String deployedFrontUrl;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins(frontUrl)
                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("http://localhost:8080")
                .allowedOrigins("https://43.202.186.65")
                .allowedOrigins("http://43.202.186.65")
                .allowedOrigins("https://goorm.ddns.net")
                .allowedOrigins("http://goorm.ddns.net")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Set-Cookie")
                .allowCredentials(true);
    }
}
