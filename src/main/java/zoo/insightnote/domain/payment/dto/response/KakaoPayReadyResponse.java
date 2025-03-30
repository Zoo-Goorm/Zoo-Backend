package zoo.insightnote.domain.payment.dto.response;

public record KakaoPayReadyResponse (
    String tid,  // 결제 고유 번호 (Transaction ID)
    String next_redirect_app_url,  // 모바일 앱에서 결제 페이지 URL
    String next_redirect_mobile_url,  // 모바일 웹에서 결제 페이지 URL
    String next_redirect_pc_url,  // PC 웹에서 결제 페이지 URL
    String android_app_scheme,  // 안드로이드 앱 스킴
    String ios_app_scheme,  // iOS 앱 스킴
    String created_at  // 결제 생성 시간
) { }
