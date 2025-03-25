package zoo.insightnote.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JoinRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    @Schema(description = "이메일", example = "abc@example.com")
    private String email;

    @NotBlank(message = "인증코드는 필수입니다.")
    @Schema(description = "인증코드", example = "123456")
    private String code;
}
