package app.web;

import app.action.model.Action;
import app.action.service.ActionService;
import app.comment.service.CommentService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.CommentRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/comments/{actionId}")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final ActionService actionService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService, ActionService actionService) {
        this.commentService = commentService;
        this.userService = userService;
        this.actionService = actionService;
    }

    @PostMapping("/new")
    public ModelAndView AddNewComment(@PathVariable UUID actionId, @Valid CommentRequest commentRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        Action action = actionService.getById(actionId);

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("action");
            modelAndView.addObject("user", user);
            modelAndView.addObject("action", action);

            modelAndView.addObject("commentRequest", commentRequest);
            return modelAndView;
        }

        commentService.createNewComment(commentRequest, action, user);

        return new ModelAndView("redirect:/actions/" + actionId);
    }

}
