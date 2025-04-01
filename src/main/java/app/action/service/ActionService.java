package app.action.service;

import app.action.model.Action;
import app.action.model.Type;
import app.action.repository.ActionRepository;
import app.animal.model.Animal;
import app.comment.model.Comment;
import app.exception.DomainException;
import app.user.model.User;
import app.web.dto.ActionRequest;
import app.web.dto.AdoptionEvent;
import app.web.dto.NeuteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ActionService {

    private final ActionRepository actionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public ActionService(ActionRepository actionRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.actionRepository = actionRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public Action createNewAction(User user, Animal animal, ActionRequest actionRequest) {

        LocalDateTime now = LocalDateTime.now();

        Action action = Action.builder()
                .user(user)
                .animal(animal)
                .type(actionRequest.getType())
                .description(actionRequest.getDescription())
                .createdOn(now)
                .build();

        actionRepository.save(action);

        if (Type.ADOPTED.equals(action.getType())) {
            AdoptionEvent adoptionEvent = AdoptionEvent.builder()
                    .animalId(animal.getId())
                    .adoptionDate(now)
                    .build();

            applicationEventPublisher.publishEvent(adoptionEvent);
        }

        if (Type.NEUTERED.equals(action.getType())) {

            NeuteredEvent neuteredEvent = NeuteredEvent.builder()
                    .animalId(animal.getId())
                    .neuteredDate(now)
                    .build();

            applicationEventPublisher.publishEvent(neuteredEvent);
        }

        return action;
    }

    public Action getById(UUID id) {

        return actionRepository.findById(id).orElseThrow(() -> new DomainException("Action with id [%s] does not exist.".formatted(id)));
    }

    public List<Comment> getAllActionComments(UUID actionId) {

        Action action = getById(actionId);

        return action.getComments();
    }

    public List<Action> getAllActions() {

        return actionRepository.findAll();
    }

    public List<Action> getActionsForFollowedAnimalByUserId(UUID userId) {

        return actionRepository.findActionsForFollowedAnimalByUserId(userId);
    }

}
