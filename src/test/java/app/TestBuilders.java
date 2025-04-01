package app;

import app.action.model.Action;
import app.animal.model.Animal;
import app.animal.model.Gender;
import app.animal.model.Status;
import app.animal.model.Type;
import app.location.model.Location;
import app.user.model.User;
import app.user.model.UserRole;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class TestBuilders {

    public static User aRandomUser() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .username("User "+UUID.randomUUID())
                .password("123123")
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        Location location = Location.builder()
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Izgrev")
                .build();

        user.setLocation(location);

        Animal animal = Animal.builder()
                .id(UUID.randomUUID())
                .type(Type.CAT)
                .isNeutered(true)
                .status(Status.STRAY)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .location(location)
                .build();

        user.setFollowedAnimals(List.of(animal));

        Action action = Action.builder()
                .id(UUID.randomUUID())
                .animal(animal)
                .type(app.action.model.Type.FED)
                .createdOn(LocalDateTime.now())
                .build();

        user.setActions(List.of(action));

        return user;
    }

    public static Animal aRandomAnimal() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .username("User")
                .password("123123")
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        Location location = Location.builder()
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Izgrev")
                .build();

        user.setLocation(location);

        Animal animal = Animal.builder()
                .id(UUID.randomUUID())
                .type(Type.CAT)
                .isNeutered(true)
                .status(Status.STRAY)
                .createdOn(LocalDateTime.now())
                .estimateDateOfBirth(LocalDate.now())
                .updatedOn(LocalDateTime.now())
                .gender(Gender.FEMALE)
                .location(location)
                .addedBy(user)
                .build();

        return animal;
    }
}
