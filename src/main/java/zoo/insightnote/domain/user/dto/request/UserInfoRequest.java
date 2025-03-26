package zoo.insightnote.domain.user.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoRequest {
    private String name;
    private String nickname;
    private String phoneNumber;
    private String occupation;
    private String job;
    private String interestCategory;
    private String snsUrl;
}
