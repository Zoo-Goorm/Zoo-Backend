package zoo.insightnote.domain.insight.mapper;

import org.springframework.data.domain.Page;
import zoo.insightnote.domain.insight.dto.response.MyInsightList;
import zoo.insightnote.domain.insight.dto.response.MyInsightListResponse;
import zoo.insightnote.domain.insight.dto.response.query.MyInsightListQuery;

import java.util.List;
import java.util.stream.Collectors;

public class MyInsightListMapper {
    public static MyInsightList toBuildMyInsightList(MyInsightListQuery dto) {
        return new MyInsightList(
                dto.insightId(),
                dto.memo(),
                dto.isPublic(),
                dto.isAnonymous(),
                dto.isDraft(),
                dto.updatedAt(),
                dto.sessionId(),
                dto.sessionName()
        );
    }

    public static MyInsightListResponse toMyListPageResponse(
            Page<MyInsightListQuery> page,
            int pageNumber,
            int pageSize
    ) {
        List<MyInsightList> content = page.getContent().stream()
                .map(MyInsightListMapper::toBuildMyInsightList)
                .collect(Collectors.toList());

        return new MyInsightListResponse(
                page.hasNext(),
                page.getTotalElements(),
                page.getTotalPages(),
                pageNumber,
                pageSize,
                content
        );
    }
}
