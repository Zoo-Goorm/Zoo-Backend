package zoo.insightnote.global.exception;

import lombok.Getter;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 행사
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 이벤트가 존재하지 않습니다."),

    // 세션
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 세션을 찾을 수 없습니다."),
    SESSION_CAPACITY_EXCEEDED(HttpStatus.BAD_REQUEST, "세션의 정원이 초과되었습니다."),

    // 인사이트 노트
    NO_EDIT_PERMISSION(HttpStatus.NOT_FOUND, "인사이트 작성자가 아닙니다, 본인이 작성한 인사이트만 수정할 수 있습니다 "),
    INSIGHT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 인사이트를 찾을 수 없습니다."),
    CANNOT_LIKE_OWN_INSIGHT(HttpStatus.BAD_REQUEST, "자신의 인사이트에는 좋아요를 누를 수 없습니다."),
    UNAUTHORIZED_USER(HttpStatus.BAD_REQUEST, "권한이 없는 사용자 입니다"),

    // 연사
    SPEAKER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 연사가 존재하지 않습니다."),

    // 키워드
    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 키워드가 존재하지 않습니다."),

    // 댓글 
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    UNAUTHORIZED_COMMENT_MODIFICATION(HttpStatus.BAD_REQUEST, "작성자만 댓글을 수정할 수 있습니다."),

    // 대댓글
    DELETED_COMMENT_CANNOT_HAVE_REPLY(HttpStatus.BAD_REQUEST, "해당 댓글이 삭제되어 댓글을 작성할 수 없습니다."),

    // 결제
    KAKAO_PAY_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "카카오페이 결제 요청 중 오류 발생"),
    KAKAO_PAY_APPROVE_FAILED(HttpStatus.BAD_REQUEST, "카카오페이 결제 승인 중 오류 발생"),
    KAKAO_PAY_CANCEL_FAILED(HttpStatus.BAD_REQUEST, "카카오페이 결제 취소 중 오류 발생"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."),

    // JSON
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 변환 오류 발생"),

    // 세션 예약
    ALREADY_RESERVED_SESSION(HttpStatus.BAD_REQUEST, "이미 예약된 세션입니다."),
    DUPLICATE_SESSION_TIME(HttpStatus.BAD_REQUEST, "세션 시간이 중복되어 신청할 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "예약된 세션이 없습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User 사용자를 찾을 수 없습니다."),
    ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),

    // QR
    QR_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "QR 코드 생성에 실패했습니다."),

    // Email
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증에 실패했습니다. 코드가 올바르지 않거나 만료되었습니다."),
    ;

    private final HttpStatus errorCode;
    private final String errorMessage;

    ErrorCode(HttpStatus errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}