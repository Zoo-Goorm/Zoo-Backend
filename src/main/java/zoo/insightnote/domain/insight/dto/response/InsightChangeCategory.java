package zoo.insightnote.domain.insight.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

public record InsightChangeCategory(
        Long id,
        String memo,
        Boolean isPublic,
        Boolean isAnonymous,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long sessionId,
        String sessionName,
        Long likeCount,
        String latestImageUrl,
        List<String> interestCategory,
        Long commentCount,
        String displayName,
        String job,
        Boolean isLiked
) {}