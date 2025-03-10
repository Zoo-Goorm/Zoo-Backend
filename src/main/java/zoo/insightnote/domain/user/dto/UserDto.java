package zoo.insightnote.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    private String name;
    private String username;
    private String role;
    private String email;
}
