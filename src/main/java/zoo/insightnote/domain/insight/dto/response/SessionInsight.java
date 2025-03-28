package zoo.insightnote.domain.insight.dto.response;

import java.time.LocalDateTime;

public record SessionInsight(
        Long id,
        String memo,
        Boolean isPublic,
        Boolean isAnonymous,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long likeCount,
        Long commentCount,
        String displayName,
        String job,
        Boolean isLiked,
        Boolean hasSpeakerComment
) {}