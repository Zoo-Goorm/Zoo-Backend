package zoo.insightnote.domain.reply.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zoo.insightnote.domain.comment.entity.Comment;
import zoo.insightnote.domain.comment.service.CommentService;
import zoo.insightnote.domain.reply.dto.ReplyRequest.Create;
import zoo.insightnote.domain.reply.dto.ReplyResponse;
import zoo.insightnote.domain.reply.entity.Reply;
import zoo.insightnote.domain.reply.repository.ReplyRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private CommentService commentService;

    @InjectMocks
    ReplyService replyService;

    Comment parentComment;

    User parentAuthor;

    User author;

    Reply reply;

    @BeforeEach
    void beforeEach() {

        parentAuthor = User.builder()
                .id(1L)
                .build();

        parentComment = Comment.builder()
                .id(1L)
                .user(parentAuthor)
                .content("작성하신 노트 잘보았습니다!")
                .build();

        author = User.builder()
                .id(2L)
                .build();


        reply = Reply.builder()
                .id(1L)
                .comment(parentComment)
                .user(author)
                .content("작성하신 노트 잘보았습니다!")
                .build();
    }

    @Test
    @DisplayName("테스트 성공 : 부모 댓글이 존재하는 경우 사용자는 대댓글을 작성할 수 있다.")
    void 사용자는_대댓글을_작성할_수_있다(){
        //given
        Create request = new Create("대댓글 작성 완료");

        when(userRepository.findById(any())).thenReturn(Optional.of(author));
        when(commentService.findCommentById(any())).thenReturn(parentComment);
        when(replyRepository.save(any())).thenReturn(reply);

        //when
        ReplyResponse.Default response = (ReplyResponse.Default) replyService.createReply(parentComment.getId(), author.getId(), request);

        //then
        Assertions.assertThat(response.parentCommentId()).isEqualTo(parentComment.getId());
        Assertions.assertThat(request.content()).isEqualTo(response.content());
        verify(replyRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("테스트 실패 : 부모 댓글이 존재하지 않은 경우 댓글을 작성할 수 없다.")
    void 이미_삭제된_댓글에는_댓글을_작성할_수_없다() {
        // given
        parentComment = Comment.builder()
                .id(999L)
                .user(parentAuthor)
                .content("작성하신 노트 잘보았습니다!")
                .build();

        Create request = new Create("대댓글 작성 완료");

        when(commentService.findCommentById(any()))
                .thenThrow(new CustomException(ErrorCode.DELETED_COMMENT_CANNOT_HAVE_REPLY));

        // when & then
        Assertions.assertThatThrownBy(() ->
                        replyService.createReply(parentComment.getId(), author.getId(), request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DELETED_COMMENT_CANNOT_HAVE_REPLY.getErrorMessage());

        verify(replyRepository, never()).save(any());
    }



}