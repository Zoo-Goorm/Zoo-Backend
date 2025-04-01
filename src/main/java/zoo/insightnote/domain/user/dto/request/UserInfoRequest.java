package zoo.insightnote.domain.user.dto.request;

public record UserInfoRequest(
        String name,
        String nickname,
        String phoneNumber,
        String occupation,
        String job,
        String interestCategory,
        String snsUrl
) { }
