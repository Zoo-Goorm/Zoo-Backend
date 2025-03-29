package zoo.insightnote.domain.insight.dto.response;

import java.util.List;

public record UserProfileResponse(
        String name,
        String email,
        List<String> interestCategory,
        List<String> linkUrls
) {}