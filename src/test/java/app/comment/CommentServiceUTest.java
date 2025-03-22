package app.comment;

import app.action.model.Action;
import app.action.model.Type;
import app.animal.model.Animal;
import app.comment.model.Comment;
import app.comment.repository.CommentRepository;
import app.comment.service.CommentService;
import app.user.model.User;
import app.web.dto.ActionRequest;
import app.web.dto.CommentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceUTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    //    public void createNewComment(@Valid CommentRequest commentRequest, Action action, User user) {
    //        Comment comment = Comment.builder()
    //                .content(commentRequest.getContent())
    //                .user(user)
    //                .createdOn(LocalDateTime.now())
    //                .action(action)
    //                .build();
    //        commentRepository.save(comment);
    //    }

    @Test
    void givenCommentRequestActionUser_whenCreateNewComment_thenCommentIsSaved() {

        //given
        CommentRequest commentRequest = CommentRequest.builder()
                .content("Great!")
                .build();
        Action action = Action.builder().build();
        User user = User.builder().build();
        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .user(user)
                .action(action)
                .build();

        when(commentRepository.save(any())).thenReturn(comment);

        //when
        commentService.createNewComment(commentRequest, action, user);

//        //then
//        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
//        verify(commentRepository).save(commentCaptor.capture());
//        Action result = commentCaptor.getValue();
//        assertEquals(result.getUser(), user);
//        assertEquals(result.get, action);
    }
}
