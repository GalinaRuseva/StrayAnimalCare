package app.action.service;

import app.action.model.Action;
import app.action.repository.ActionRepository;
import app.animal.model.Animal;
import app.comment.model.Comment;
import app.exception.DomainException;
import app.user.model.User;
import app.web.dto.ActionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ActionService {

    private final ActionRepository actionRepository;

    @Autowired
    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public void createNewAction(User user, Animal animal, ActionRequest actionRequest) {

        Action action = Action.builder()
                .user(user)
                .animal(animal)
                .type(actionRequest.getType())
                .description(actionRequest.getDescription())
                .createdOn(LocalDateTime.now())
                .build();

        actionRepository.save(action);
    }

    public Action getById(UUID id) {

        return actionRepository.findById(id).orElseThrow(() -> new DomainException("Action with id [%s] does not exist.".formatted(id)));
    }

    public List<Comment> getAllActionComments(UUID actionId) {

        Action action = getById(actionId);

        return action.getComments();
    }
}
