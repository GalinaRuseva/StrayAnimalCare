package app;

import app.animal.model.Animal;
import app.animal.model.Gender;
import app.animal.model.Type;
import app.animal.service.AnimalService;
import app.location.model.Location;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.AnimalRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final AnimalService animalService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    //@Autowired
    public DatabaseInitializer(UserService userService, AnimalService animalService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userService = userService;
        this.animalService = animalService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void run(String... args) throws Exception {


        AnimalRequest animalRequest1 = new AnimalRequest();
        //Type.CAT, "Mishi", "01.08.2016", Gender.FEMALE, "sweet cat"

        animalRequest1.setType(Type.CAT);
        animalRequest1.setName("Mishi");
        animalRequest1.setDateOfBirth(LocalDate.of(2016, 8, 1));
        animalRequest1.setGender(Gender.FEMALE);
        animalRequest1.setInformation("Very nice cat");
        animalRequest1.setLocation(new Location("Bulgaria", "Sofia", "Mladost 1"));

        AnimalRequest animalRequest2 = AnimalRequest.builder()
                .type(Type.CAT)
                .name("Bobi")
                .dateOfBirth(LocalDate.of(2016, 8, 1))
                .gender(Gender.MALE)
                .information("Grey annoying cat")
                .location(new Location("Bulgaria", "Shumen", "Alen Mak"))
                .build();

        Animal animal1 = animalService.createAnimal(animalRequest1);
        Animal animal2 = animalService.createAnimal(animalRequest2);

        /* RegisterRequest registerRequest1 = RegisterRequest.builder()
                .username("Galina")
                .password("123456")
                .build();

        RegisterRequest registerRequest2 = RegisterRequest.builder()
                .username("Nikolay")
                .password("123456")
                .build();

        User registeredUser1 = userService.register(registerRequest1);
        User registeredUser2 = userService.register(registerRequest2); */

/*

        User registeredUser1 = User.builder()
                .username("Galina")
                .firstName("Galina")
                .lastName("Ruseva")
                .email("galinagyordanova@gmail.com")
                .phoneNumber("0899164065")
                .password(passwordEncoder.encode("123456"))
                .role(UserRole.ADMIN)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .location(new Location("Bulgaria", "Sofia", "Mladost 1"))
                .build();

        userRepository.save(registeredUser1);

        User registeredUser2 = User.builder()
                .username("Nikolay")
                .firstName("Nikolay")
                .lastName("Rusev")
                .email("nikolay@gmail.com")
                .phoneNumber("0899111111")
                .password(passwordEncoder.encode("123456"))
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .location(new Location("Bulgaria", "Shumen", "Alen Mak"))
                .build();

        userRepository.save(registeredUser2);

        userService.followAnimal(animal1, registeredUser1.getId());

    }
} */


