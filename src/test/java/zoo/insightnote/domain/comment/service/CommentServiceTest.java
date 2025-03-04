package zoo.insightnote.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zoo.insightnote.domain.comment.dto.CommentRequest;
import zoo.insightnote.domain.comment.dto.CommentRequest.Create;
import zoo.insightnote.domain.comment.dto.CommentResponse;
import zoo.insightnote.domain.comment.entity.Comment;
import zoo.insightnote.domain.comment.repository.CommentRepository;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.insight.repository.InsightRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.global.exception.CustomException;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @Mock
    InsightRepository insightRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CommentService commentService;

    Insight insight;

    User author;

    Comment comment;

    @BeforeEach
    void beforeEach() {
        insight = Insight
                .builder()
                .id(1L)
                .build();

        author = User.builder()
                .id(1L)
                .build();

        comment = Comment.builder()
                .id(1L)
                .insight(insight)
                .user(author)
                .content("작성하신 노트 잘보았습니다!")
                .build();
    }

    @Test
    @DisplayName("테스트 성공 : 유효한 요청인 경우 인사이트 노트에 댓글을 작성할 수 있다.")
    void 사용자는_댓글을_작성할_수_있다() {
        // given
        CommentRequest.Create request = new Create("작성하신 노트 잘보았습니다!");

        // when
        when(insightRepository.findById(any(Long.class))).thenReturn(Optional.of(insight));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(author));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponse.Default response = (CommentResponse.Default) commentService.createComment(insight.getId(), author.getId(), request);

        // then
        assertThat(response.commentId()).isEqualTo(1L);
        assertThat(response.content()).isEqualTo("작성하신 노트 잘보았습니다!");
    }

    @Test
    @DisplayName("테스트 실패 : 존재하지 않은 인사이트 노트에 댓글을 작성할 수 없다.")
    void 댓글_작성_실패_존재하지_않은_인사이트_노트() {
        // given
        CommentRequest.Create request = new Create("작성하신 노트 잘보았습니다!");

        // when
        when(insightRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> commentService.createComment(insight.getId(), author.getId(), request))
                .isInstanceOf(CustomException.class)
                .hasMessage("인사이트 노트를 찾을 수 없음");
    }

    @Test
    @DisplayName("테스트 실패 : 로그인되지 않은 사용자는 댓글을 작성할 수 없다.")
    void 댓글_작성_실패_비회원() {
        // given
        CommentRequest.Create request = new Create("작성하신 노트 잘보았습니다!");

        // when
        when(insightRepository.findById(any(Long.class))).thenReturn(Optional.of(insight));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> commentService.createComment(insight.getId(), author.getId(), request))
                .isInstanceOf(CustomException.class)
                .hasMessage("사용자를 찾을 수 없음");
    }

    @Test
    @DisplayName("테스트 성공: 인사이트 노트의 모든 댓글을 조회한다.")
    void 인사이트_노트_모든_댓글_조회() {
        // given
        List<Comment> comments = Arrays.asList(
                new Comment(1L, author, insight, "댓글1"),
                new Comment(2L, author, insight, "댓글2")
        );

        when(commentRepository.findAllByInsightId(eq(1L))).thenReturn(comments);

        // when
        List<CommentResponse> result = commentService.findCommentsByInsightId(1L);

        // then
        assertThat(result).hasSize(2);
    }


}