package zoo.insightnote.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  
    // 행사
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 이벤트가 존재하지 않습니다."),;

    // 댓글 
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    UNAUTHORIZED_COMMENT_MODIFICATION(HttpStatus.BAD_REQUEST, "작성자만 댓글을 수정할 수 있습니다.");

    private final HttpStatus errorCode;
    private final String errorMessage;

    ErrorCode(HttpStatus errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
