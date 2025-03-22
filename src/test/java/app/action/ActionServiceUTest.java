package app.action;

import app.action.model.Action;
import app.action.model.Type;
import app.action.repository.ActionRepository;
import app.action.service.ActionService;
import app.animal.model.Animal;
import app.comment.model.Comment;
import app.comment.repository.CommentRepository;
import app.comment.service.CommentService;
import app.exception.DomainException;
import app.user.model.User;
import app.web.dto.ActionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActionServiceUTest {

    @Mock
    private ActionRepository actionRepository;
//    @Mock
//    private CommentRepository commentRepository;
//    @Mock
//    private CommentService commentService;

    @InjectMocks
    private ActionService actionService;

    @Test
    void givenMissingActionFromDatabase_whenLoadGetById_thenExceptionIsThrown() {

        //given
        UUID actionId = UUID.randomUUID();
        when(actionRepository.findById(actionId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(DomainException.class, () -> actionService.getById(actionId));
    }

    @Test
    void givenExistingActionFromDatabase_whenLoadGetById_thenReturnAction() {

        //given
        Action action = Action.builder()
                .id(UUID.randomUUID())
                .build();
        when(actionRepository.findById(action.getId())).thenReturn(Optional.of(action));

        //when
        Action result = actionService.getById(action.getId());

        //then
        assertEquals(result, action);
    }

    @Test
    void givenExistingActionWithoutComments_whenGetAllActionComments_thenReturnEmptyList() {

        //given
        Action action = Action.builder()
                .id(UUID.randomUUID())
                .build();
        action.setComments(new ArrayList<>());
        when(actionRepository.findById(action.getId())).thenReturn(Optional.of(action));

        //when
        List<Comment> result = actionService.getAllActionComments(action.getId());

        //then
        assertTrue(action.getComments().isEmpty());
        assertThat(action.getComments()).hasSize(0);
    }

    @Test
    void givenExistingActionWithComments_whenGetAllActionComments_thenReturnListOfComments() {

        //given
        Action action = Action.builder()
                .id(UUID.randomUUID())
                .build();
        action.setComments(new ArrayList<>());
        action.getComments().add(new Comment());
        when(actionRepository.findById(action.getId())).thenReturn(Optional.of(action));

        //when
        List<Comment> result = actionService.getAllActionComments(action.getId());

        //then
        assertThat(action.getComments()).hasSize(1);
    }

    @Test
    void givenExistingActionsInDataBase_whenGetAllAction_thenReturnListOfActions() {

        //given
        List<Action> actionsListList = List.of(new Action(), new Action(), new Action());
        when(actionRepository.findAll()).thenReturn(actionsListList);

        //when
        List<Action> result = actionService.getAllActions();

        //then
        assertThat(result).hasSize(3);
    }

    @Test
    void givenEmptyDataBase_whenGetAllAction_thenReturnEmptyList() {

        //given
        when(actionRepository.findAll()).thenReturn(new ArrayList<>());

        //when
        List<Action> result = actionService.getAllActions();

        //then
        assertThat(result).hasSize(0);
    }

    @Test
    void givenExistingActionsInDataBaseForFollowedAnimal_whenGetActionsForFollowedAnimal_thenReturnListOfActions() {

        //given
        UUID userId = UUID.randomUUID();
        List<Action> actionsListList = List.of(new Action(), new Action(), new Action());
        when(actionRepository.findActionsForFollowedAnimalByUserId(userId)).thenReturn(actionsListList);

        //when
        List<Action> result = actionService.getActionsForFollowedAnimalByUserId(userId);

        //then
        assertThat(result).hasSize(3);
    }

    @Test
    void givenMissingActionDataBaseForFollowedAnimal_whenGetActionsForFollowedAnimal_thenReturnEmptyList() {

        //given
        UUID userId = UUID.randomUUID();
        when(actionRepository.findActionsForFollowedAnimalByUserId(userId)).thenReturn(new ArrayList<>());

        //when
        List<Action> result = actionService.getActionsForFollowedAnimalByUserId(userId);

        //then
        assertThat(result).hasSize(0);
    }

    @Test
    void givenUserAnimalActionRequest_whenCreateNewAction_thenActionIsSaved() {

        //given
        User user = User.builder().build();
        Animal animal = Animal.builder().build();
        ActionRequest actionRequest = ActionRequest.builder()
                .type(Type.ADOPTED)
                .description("The animal is adopted!")
                .build();
        Action action = Action.builder()
                .type(actionRequest.getType())
                .description(actionRequest.getDescription())
                .build();
        when(actionRepository.save(any())).thenReturn(action);

        //when
        actionService.createNewAction(user, animal, actionRequest);

        //then
        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(actionRepository).save(actionCaptor.capture());
        Action result = actionCaptor.getValue();
        assertEquals(result.getUser(), user);
        assertEquals(result.getAnimal(), animal);
    }

}
