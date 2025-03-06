package zoo.insightnote.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //Comment ErrorCode
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    UNAUTHORIZED_COMMENT_MODIFICATION(HttpStatus.BAD_REQUEST, "작성자만 댓글을 수정할 수 있습니다."),

    //Payment ErrorCode
    KAKAO_PAY_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "카카오페이 결제 요청 중 오류 발생"),
    KAKAO_PAY_APPROVE_FAILED(HttpStatus.BAD_REQUEST, "카카오페이 결제 승인 중 오류 발생"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."),

    // JSON ErrorCode
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 변환 오류 발생");

    private final HttpStatus errorCode;
    private final String errorMessage;

    ErrorCode(HttpStatus errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}