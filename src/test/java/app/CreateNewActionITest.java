package app;

import app.action.model.Action;
import app.action.repository.ActionRepository;
import app.action.service.ActionService;
import app.animal.model.Animal;
import app.animal.model.Gender;
import app.animal.model.Status;
import app.animal.model.Type;
import app.animal.repository.AnimalRepository;
import app.animal.service.AnimalService;
import app.location.model.Location;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.ActionRequest;
import app.web.dto.AnimalRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class CreateNewActionITest {

    @Autowired
    private UserService userService;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private UserRepository userRepository;

    //what do I need to create action
    // - register user
    // - add new animal

    @Test
    void createNewAction_happyPath() {

        //given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("nikolay123")
                .password("123123")
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Izgrev")
                .build();

        User registeredUser = userService.register(registerRequest);

        Animal animal = Animal.builder()
                .type(Type.CAT)
                .name("Mishi123")
                .estimateDateOfBirth(LocalDate.of(2021, 12, 15))
                .gender(Gender.FEMALE)
                .isNeutered(true)
                .status(Status.STRAY)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .location(new Location("Bulgaria", "Sofia", "Izgrev"))
                .build();

        animalRepository.save(animal);

        ActionRequest actionRequest = ActionRequest.builder()
                .type(app.action.model.Type.FED)
                .description("Mishi have breakfast")
                .build();

        //when
        Action action = actionService.createNewAction(registeredUser, animal, actionRequest);


        //then
        Optional<Action> optionalAction = actionRepository.findById(action.getId());
        Action result = optionalAction.get();
        List<Action> all = actionRepository.findAll();

        assertEquals(result.getId(), action.getId());
        assertThat(all.size(), is(1));
//        assertEquals(result.getAnimal(), action.getAnimal());
//        assertEquals(result.getType(), action.getType());
    }





}
