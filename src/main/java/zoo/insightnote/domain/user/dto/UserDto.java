package zoo.insightnote.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDto {

    private String role;
    private String name;
    private String username;
}
