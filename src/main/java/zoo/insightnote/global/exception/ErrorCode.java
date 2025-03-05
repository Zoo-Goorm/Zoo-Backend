package zoo.insightnote.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 이벤트가 존재하지 않습니다."),;



    private final HttpStatus errorCode;
    private final String errorMessage;

    ErrorCode(HttpStatus errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
