package zoo.insightnote.domain.insight.mapper;

import org.springframework.data.domain.Page;
import zoo.insightnote.domain.insight.dto.response.MyInsightList;
import zoo.insightnote.domain.insight.dto.response.MyInsightListResponse;
import zoo.insightnote.domain.insight.dto.response.query.MyInsightListQuery;

import java.util.List;
import java.util.stream.Collectors;

public class MyInsightListMapper {
    public static MyInsightList toBuildMyInsightList(MyInsightListQuery dto) {
        return MyInsightList.builder()
                .insightId(dto.getInsightId())
                .memo(dto.getMemo())
                .isPublic(dto.getIsPublic())
                .isAnonymous(dto.getIsAnonymous())
                .updatedAt(dto.getUpdatedAt())
                .sessionId(dto.getSessionId())
                .sessionName(dto.getSessionName())
                .build();
    }

    public static MyInsightListResponse toMyListPageResponse(
            Page<MyInsightListQuery> page,
            int pageNumber,
            int pageSize
    ) {
        List<MyInsightList> content = page.getContent().stream()
                .map(MyInsightListMapper::toBuildMyInsightList)
                .collect(Collectors.toList());

        return MyInsightListResponse.builder()
                .hasNext(page.hasNext())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .content(content)
                .build();
    }
}
