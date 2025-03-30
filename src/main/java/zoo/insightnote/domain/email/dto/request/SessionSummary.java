package zoo.insightnote.domain.email.dto.request;

public record SessionSummary(
    String sessionName,
    String timeRange,
    String speakerName
) { }
