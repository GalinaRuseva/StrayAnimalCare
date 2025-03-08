package app.comment.service;

import app.action.model.Action;
import app.comment.model.Comment;
import app.comment.repository.CommentRepository;
import app.user.model.User;
import app.web.dto.CommentRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void createNewComment(@Valid CommentRequest commentRequest, Action action, User user) {

        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .user(user)
                .createdOn(LocalDateTime.now())
                .action(action)
                .build();

        commentRepository.save(comment);
    }
}
