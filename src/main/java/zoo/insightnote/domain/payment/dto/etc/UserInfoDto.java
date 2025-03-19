package zoo.insightnote.domain.payment.dto.etc;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserInfoDto {
    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String job;         // 직업

    @NotBlank
    private String occupation;  // 직군

    @NotBlank
    private String interestCategory;

    @JsonProperty("online")
    // 자바에서 직렬화를 할 때 is 접두사가 붙으면 제거하는 특징이 있음
    // 그래서 Jackson에서 직렬화/역직렬화가 될 때 접두사가 빠져서 online으로 변환됨
    // JsonProperty 이용해서 online으로 받아들이도록 함
    private boolean isOnline;
}
