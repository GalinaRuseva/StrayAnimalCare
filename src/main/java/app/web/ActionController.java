package app.web;

import app.action.model.Action;
import app.action.service.ActionService;
import app.animal.model.Animal;
import app.animal.service.AnimalService;
import app.comment.model.Comment;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.ActionRequest;
import app.web.dto.CommentRequest;
import app.web.dto.HealthRecordRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/actions")
public class ActionController {

    private final ActionService actionService;
    private final AnimalService animalService;
    private final UserService userService;

    @Autowired
    public ActionController(ActionService actionService, AnimalService animalService, UserService userService) {
        this.actionService = actionService;
        this.animalService = animalService;
        this.userService = userService;
    }

    @PostMapping("/{animalId}/new")
    public ModelAndView createNewAction(@PathVariable UUID animalId, @Valid ActionRequest actionRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());
        Animal animal = animalService.getById(animalId);

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("animal-profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("animal", animal);
            modelAndView.addObject("actionRequest", actionRequest);
            modelAndView.addObject("healthRecordRequest", new HealthRecordRequest());
            return modelAndView;
        }

        actionService.createNewAction(user, animal, actionRequest);

        return new ModelAndView("redirect:/animals/" + animalId + "/profile");
    }

    @GetMapping("/{actionId}")
    public ModelAndView getActionPage(@PathVariable UUID actionId, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        Action action = actionService.getById(actionId);
        List<Comment> allActionComments = actionService.getAllActionComments(actionId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("action");
        modelAndView.addObject("action", action);
        modelAndView.addObject("allActionComments", allActionComments);
        modelAndView.addObject("user", user);
        modelAndView.addObject("commentRequest", new CommentRequest());

        return modelAndView;
    }
}
