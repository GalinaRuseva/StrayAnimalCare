package app;

import app.animal.model.Animal;
import app.animal.model.Gender;
import app.animal.model.Type;
import app.animal.repository.AnimalRepository;
import app.animal.service.AnimalService;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.AnimalRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class FollowAnimalITest {

    @Autowired
    private UserService userService;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Test
    void FollowAnimal_HappyPath() {

        //given
        AnimalRequest animalRequest = AnimalRequest.builder()
                .type(Type.DOG)
                .name("Maksi")
                .estimateDateOfBirth(LocalDate.now().minusYears(2))
                .gender(Gender.MALE)
                .neutered(true)
                .information("Big and friendly black dog")
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Izgrev")
                .build();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Valentin3")
                .password("12345678")
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Izgrev")
                .build();

        User user = userService.register(registerRequest);
        Animal animal = animalService.createAnimal(animalRequest, user);

        //when
        userService.followAnimal(animal, user);

        //then
        assertEquals(user.getFollowedAnimals().get(0).getId(), animal.getId());
    }


}
