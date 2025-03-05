package zoo.insightnote.domain.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayReadyResponseDto {
    private String tid;  // 결제 고유 번호 (Transaction ID)
    private String next_redirect_app_url;  // 모바일 앱에서 결제 페이지 URL
    private String next_redirect_mobile_url;  // 모바일 웹에서 결제 페이지 URL
    private String next_redirect_pc_url;  // PC 웹에서 결제 페이지 URL
    private String android_app_scheme;  // 안드로이드 앱 스킴
    private String ios_app_scheme;  // iOS 앱 스킴
    private String created_at;  // 결제 생성 시간
}
