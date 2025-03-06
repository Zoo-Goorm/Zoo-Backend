package zoo.insightnote.domain.reply.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import zoo.insightnote.domain.comment.entity.Comment;
import zoo.insightnote.domain.comment.service.CommentService;
import zoo.insightnote.domain.reply.dto.ReplyRequest.Create;
import zoo.insightnote.domain.reply.dto.ReplyRequest.Update;
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

    @Test
    @DisplayName("테스트 성공 : 사용자는 댓글에 달린 대댓글을 모두 조회할 수 있다.")
    void 모든_대댓글_조회_성공() {
        //given
        List<Reply> replies = Arrays.asList(
                new Reply(1L, author, parentComment, "대댓글1"),
                new Reply(2L, author, parentComment, "대댓글1")
        );

        when(replyRepository.findAllByCommentId(parentComment.getId())).thenReturn(replies);

        //when
        List<ReplyResponse> responses = replyService.findRepliesByCommentId(parentComment.getId());

        //then
        Assertions.assertThat(replies.size()).isEqualTo(responses.size());
    }

    @Test
    @DisplayName("테스트 성공 : 사용자가 대댓글을 정상적으로 수정할 수 있다.")
    void 사용자는_대댓글을_정상적으로_수정할_수_있다() {
        // given
        Update request = new Update("수정된 대댓글 내용");

        when(userRepository.findById(any())).thenReturn(Optional.of(author));
        when(replyRepository.findById(any())).thenReturn(Optional.of(reply));

        // when
        ReplyResponse.Default response = (ReplyResponse.Default) replyService.updateReply(parentComment.getId(), reply.getId(), author.getId(), request);

        // then
        Assertions.assertThat(response.content()).isEqualTo(request.content());
    }

    @Test
    @DisplayName("테스트 실패 : 사용자가 대댓글 작성자가 아니면 수정할 수 없다.")
    void 사용자는_대댓글_작성자가_아니면_수정할_수_없다() {
        // given
        Update request = new Update("수정된 대댓글 내용");
        User anotherUser = User.builder().id(3L).build();

        when(userRepository.findById(any())).thenReturn(Optional.of(anotherUser));
        when(replyRepository.findById(any())).thenReturn(Optional.of(reply));

        // when & then
        Assertions.assertThatThrownBy(() ->
                        replyService.updateReply(parentComment.getId(), reply.getId(), anotherUser.getId(), request))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("테스트 성공 : 사용자가 대댓글을 정상적으로 삭제할 수 있다.")
    void 사용자는_대댓글을_정상적으로_삭제할_수_있다() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.of(author));
        when(replyRepository.findById(any())).thenReturn(Optional.of(reply));

        // when
        ReplyResponse response = replyService.deleteReply(parentComment.getId(), reply.getId(), author.getId());

        // then
        verify(replyRepository, times(1)).deleteById(reply.getId());
    }

    @Test
    @DisplayName("테스트 실패 : 사용자가 대댓글 작성자가 아니면 삭제할 수 없다.")
    void 사용자는_대댓글_작성자가_아니면_삭제할_수_없다() {
        // given
        User anotherUser = User.builder().id(3L).build();

        when(userRepository.findById(any())).thenReturn(Optional.of(anotherUser));
        when(replyRepository.findById(any())).thenReturn(Optional.of(reply));

        // when & then
        Assertions.assertThatThrownBy(() ->
                        replyService.deleteReply(parentComment.getId(), reply.getId(), anotherUser.getId()))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("테스트 실패 : 댓글이 이미 삭제된 경우 대댓글을 삭제할 수 없다.")
    void 삭제된_댓글에는_대댓글을_삭제할_수_없다() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.of(author));

        parentComment = Comment.builder()
                .id(999L)
                .user(parentAuthor)
                .content("삭제된 댓글")
                .build();

        // when & then
        Assertions.assertThatThrownBy(() ->
                        replyService.deleteReply(parentComment.getId(), reply.getId(), author.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DELETED_COMMENT_CANNOT_HAVE_REPLY.getErrorMessage());

    }





}