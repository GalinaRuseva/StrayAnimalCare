package app.dataBaseInitializerService;

import app.action.model.Action;
import app.action.repository.ActionRepository;
import app.action.service.ActionService;
import app.animal.model.Animal;
import app.animal.model.Gender;
import app.animal.model.Status;
import app.animal.model.Type;
import app.animal.repository.AnimalRepository;
import app.animal.service.AnimalService;
import app.comment.repository.CommentRepository;
import app.comment.service.CommentService;
import app.location.model.Location;
import app.picture.repository.PictureRepository;
import app.picture.service.PictureService;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.ActionRequest;
import app.web.dto.AnimalRequest;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class DataBaseInitializerService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final ActionRepository actionRepository;
    private final CommentRepository commentRepository;
    private final PictureRepository pictureRepository;
    private final PasswordEncoder passwordEncoder;



    @Autowired
    public DataBaseInitializerService(UserRepository userRepository, AnimalRepository animalRepository, ActionRepository actionRepository, CommentRepository commentRepository, PictureRepository pictureRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.actionRepository = actionRepository;
        this.commentRepository = commentRepository;
        this.pictureRepository = pictureRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.findAll().isEmpty()) {
            return;
        }

        Location location1 = Location.builder()
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Mladost 1")
                .build();

        Location location2 = Location.builder()
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Mladost 2")
                .build();

        Location location3 = Location.builder()
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Izgrev")
                .build();

        //create User with role ADMIN
        User userGalina = User.builder()
                .username("galina")
                .password(passwordEncoder.encode("123123"))
                .firstName("Galina")
                .lastName("Ruseva")
                .email("galina@gmail.com")
                .phoneNumber("08991640666")
                .role(UserRole.ADMIN)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .actions(new ArrayList<>())
                .location(location1)
                .followedAnimals(new ArrayList<>())
                .build();

        userRepository.save(userGalina);


        //create User with role USER
        User userRostislav = User.builder()
                .username("rostislav")
                .password(passwordEncoder.encode("123123"))
                .firstName("Rostislav")
                .lastName("Petrov")
                .email("rostislav@gmail.com")
                .phoneNumber("089923440666")
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .actions(new ArrayList<>())
                .location(location2)
                .followedAnimals(new ArrayList<>())
                .build();

        userRepository.save(userRostislav);

        //create User with role USER
        User userNikolay = User.builder()
                .username("nikolay")
                .password(passwordEncoder.encode("123123"))
                .firstName("Nikolay")
                .lastName("Ivanov")
                .email("nikolay@gmail.com")
                .phoneNumber("089912340666")
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .actions(new ArrayList<>())
                .location(location3)
                .followedAnimals(new ArrayList<>())
                .build();

        userRepository.save(userNikolay);

        // create animal - Mishi
        Animal animalMishi = Animal.builder()
                .type(Type.CAT)
                .name("Mishi")
                .estimateDateOfBirth(LocalDate.now().minusYears(3))
                .gender(Gender.FEMALE)
                .isNeutered(true)
                .status(Status.STRAY)
                .pictures(new ArrayList<>())
                .information("Very fluffy, white cat.")
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .actions(new ArrayList<>())
                .healthRecords(new ArrayList<>())
                .location(location1)
                .addedBy(userGalina)
                .build();

        animalRepository.save(animalMishi);

        // create animal - Bobi
        Animal animalBobi = Animal.builder()
                .type(Type.CAT)
                .name("Bobi")
                .estimateDateOfBirth(LocalDate.now().minusYears(2).minusMonths(3))
                .gender(Gender.MALE)
                .isNeutered(true)
                .status(Status.STRAY)
                .pictures(new ArrayList<>())
                .information("Grey cat, with green eyes")
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .actions(new ArrayList<>())
                .healthRecords(new ArrayList<>())
                .location(location2)
                .addedBy(userRostislav)
                .build();

        animalRepository.save(animalBobi);

        // create animal - Bobi
        Animal animalMaks = Animal.builder()
                .type(Type.DOG)
                .name("Maks")
                .estimateDateOfBirth(LocalDate.now().minusYears(1).minusMonths(4))
                .gender(Gender.MALE)
                .isNeutered(true)
                .status(Status.STRAY)
                .pictures(new ArrayList<>())
                .information("Very friendly and nice dog.")
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .actions(new ArrayList<>())
                .healthRecords(new ArrayList<>())
                .location(location3)
                .addedBy(userNikolay)
                .build();

        animalRepository.save(animalMaks);

        // add actions for Mishi
        Action actionMishiFed = Action.builder()
                .user(userGalina)
                .animal(animalMishi)
                .type(app.action.model.Type.FED)
                .description("Mishi had eaten two pouches and fresh water is left.")
                .createdOn(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        actionRepository.save(actionMishiFed);

        Action actionMishiVaccinated = Action.builder()
                .user(userGalina)
                .animal(animalMishi)
                .type(app.action.model.Type.VACCINATED)
                .description("Mishi had her yearly vaccine.")
                .createdOn(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        actionRepository.save(actionMishiVaccinated);

        Action actionMishiAntiparasitic = Action.builder()
                .user(userGalina)
                .animal(animalMishi)
                .type(app.action.model.Type.PARASITE_PROTECTED)
                .description("Mishi is parasite protected with NexGard Combo.")
                .createdOn(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        actionRepository.save(actionMishiAntiparasitic);

        // add actions for Bobi
        Action actionBobiFed = Action.builder()
                .user(userRostislav)
                .animal(animalBobi)
                .type(app.action.model.Type.FED)
                .description("Bobi had eaten one pouche, dry cat food and fresh water is left.")
                .createdOn(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        actionRepository.save(actionBobiFed);


        Action actionBobiVaccinated = Action.builder()
                .user(userRostislav)
                .animal(animalBobi)
                .type(app.action.model.Type.VACCINATED)
                .description("Bobi had his yearly vaccine.")
                .createdOn(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        actionRepository.save(actionBobiVaccinated);

        Action actionBobiAntiparasitic = Action.builder()
                .user(userRostislav)
                .animal(animalBobi)
                .type(app.action.model.Type.PARASITE_PROTECTED)
                .description("Bobi is parasite protected with NexGard Spectra.")
                .createdOn(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        actionRepository.save(actionBobiAntiparasitic);

        // add actions for Maks
        Action actionMaksFed = Action.builder()
                .user(userNikolay)
                .animal(animalMaks)
                .type(app.action.model.Type.FED)
                .description("Maks had eaten dry dog food, dog treat and fresh water is left.")
                .createdOn(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        actionRepository.save(actionMaksFed);

        Action actionMaksVaccinated = Action.builder()
                .user(userNikolay)
                .animal(animalMaks)
                .type(app.action.model.Type.VACCINATED)
                .description("Maks had his yearly vaccine.")
                .createdOn(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        actionRepository.save(actionMaksVaccinated);

        Action actionMaksAntiparasitic = Action.builder()
                .user(userNikolay)
                .animal(animalMaks)
                .type(app.action.model.Type.PARASITE_PROTECTED)
                .description("Maks is parasite protected with Simparica TRIO.")
                .createdOn(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        actionRepository.save(actionMaksAntiparasitic);

        Action actionMaksNewHouse = Action.builder()
                .user(userNikolay)
                .animal(animalMaks)
                .type(app.action.model.Type.OTHER)
                .description("Maks has new god hose!")
                .createdOn(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        actionRepository.save(actionMaksNewHouse);

        // add comment to action



    }
}
