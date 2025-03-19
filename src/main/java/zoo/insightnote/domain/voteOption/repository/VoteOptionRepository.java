package zoo.insightnote.domain.voteOption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.voteOption.entity.VoteOption;

import java.util.List;

@Repository
public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {

    // 특정 인사이트에 해당하는 투표 옵션 가져오기
    List<VoteOption> findByInsight(Insight insight);

    // 특정 인사이트에 연결된 모든 투표 옵션 삭제

    @Query("DELETE FROM VoteOption v WHERE v.insight = :insight")
    void deleteByInsight(@Param("insight") Insight insight);
}