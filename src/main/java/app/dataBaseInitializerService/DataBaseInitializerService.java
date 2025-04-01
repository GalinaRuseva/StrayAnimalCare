package app.dataBaseInitializerService;

import app.action.model.Action;
import app.action.repository.ActionRepository;
import app.animal.model.Animal;
import app.animal.model.Gender;
import app.animal.model.Status;
import app.animal.model.Type;
import app.animal.repository.AnimalRepository;
import app.comment.model.Comment;
import app.comment.repository.CommentRepository;
import app.healthRecord.model.HealthRecord;
import app.healthRecord.repository.HealthRecordRepository;
import app.location.model.Location;
import app.picture.repository.PictureRepository;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
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
    private final HealthRecordRepository healthRecordRepository;


    @Autowired
    public DataBaseInitializerService(UserRepository userRepository, AnimalRepository animalRepository, ActionRepository actionRepository, CommentRepository commentRepository, PictureRepository pictureRepository, PasswordEncoder passwordEncoder, HealthRecordRepository healthRecordRepository) {
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.actionRepository = actionRepository;
        this.commentRepository = commentRepository;
        this.pictureRepository = pictureRepository;
        this.passwordEncoder = passwordEncoder;
        this.healthRecordRepository = healthRecordRepository;
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
                .neighborhood("Lozenets")
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

        //create User with role USER, active before 3 months
        User userVladislav = User.builder()
                .username("vladislav")
                .password(passwordEncoder.encode("123123"))
                .firstName("Vladislav")
                .lastName("Petkov")
                .email("vladislav@gmail.com")
                .phoneNumber("089233440666")
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .lastLogin(LocalDateTime.now().minusMonths(4))
                .actions(new ArrayList<>())
                .location(location2)
                .followedAnimals(new ArrayList<>())
                .build();

        userRepository.save(userVladislav);

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
                .isNeutered(false)
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
                .isNeutered(false)
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
                .description("Maks has new dog house!")
                .createdOn(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        actionRepository.save(actionMaksNewHouse);

        // add comments to action actionMishiAntiparasitic

        Comment commentRostislav = Comment.builder()
                .content("Thank you for protecting her!")
                .user(userRostislav)
                .createdOn(LocalDateTime.now())
                .action(actionMishiAntiparasitic)
                .build();

        commentRepository.save(commentRostislav);


        Comment commentGalina = Comment.builder()
                .content("I will give her next dose after 3 months.")
                .user(userGalina)
                .createdOn(LocalDateTime.now())
                .action(actionMishiAntiparasitic)
                .build();

        commentRepository.save(commentGalina);


        // add comments to action actionBobiFed

        Comment commentGalina1 = Comment.builder()
                .content("Good to you!")
                .user(userGalina)
                .createdOn(LocalDateTime.now())
                .action(actionBobiFed)
                .build();

        commentRepository.save(commentGalina1);

        Comment commentRostislav1 = Comment.builder()
                .content("No worries. But can you please feed him this evening?")
                .user(userRostislav)
                .createdOn(LocalDateTime.now())
                .action(actionBobiFed)
                .build();

        commentRepository.save(commentRostislav1);


        Comment commentGalina2 = Comment.builder()
                .content("Of course, I will feed him!")
                .user(userGalina)
                .createdOn(LocalDateTime.now())
                .action(actionBobiFed)
                .build();

        commentRepository.save(commentGalina2);

        // add comments to action actionMaksNewHouse

        Comment commentGalina3 = Comment.builder()
                .content("Very nice house, did he liked it?")
                .user(userGalina)
                .createdOn(LocalDateTime.now())
                .action(actionMaksNewHouse)
                .build();

        commentRepository.save(commentGalina3);

        Comment commentNikolay = Comment.builder()
                .content("Thanks! Yes, he loves it.")
                .user(userNikolay)
                .createdOn(LocalDateTime.now())
                .action(actionMaksNewHouse)
                .build();

        commentRepository.save(commentNikolay);


        Comment commentRostislav3 = Comment.builder()
                .content("Great!")
                .user(userRostislav)
                .createdOn(LocalDateTime.now())
                .action(actionMaksNewHouse)
                .build();

        commentRepository.save(commentRostislav3);

        // add health record for Mishi
        HealthRecord healthRecordMishi = HealthRecord.builder()
                .animal(animalMishi)
                .createdOn(LocalDateTime.now())
                .description("Routine physical examination. Vital signs within normal limits. Good overall condition. No abnormalities observed.")
                .veterinaryInformation("Dr. Dimitrov, Vet clinik, Bulgaria, Sofia.")
                .build();

        healthRecordRepository.save(healthRecordMishi);

        // add health record for Bobi
        HealthRecord healthRecordBobi = HealthRecord.builder()
                .animal(animalBobi)
                .createdOn(LocalDateTime.now())
                .description("Wound cleaned and dressed, healing as expected, follow-up appointment scheduled.")
                .veterinaryInformation("Dr. Dimitrov, Vet clinik, Bulgaria, Sofia.")
                .build();

        healthRecordRepository.save(healthRecordBobi);

        // add health record for Maks
        HealthRecord healthRecordMaks = HealthRecord.builder()
                .animal(animalMaks)
                .createdOn(LocalDateTime.now())
                .description("Routine physical examination. Vital signs within normal limits. Good overall condition. No abnormalities observed.")
                .veterinaryInformation("Dr. Dimitrov, Vet clinik, Bulgaria, Sofia.")
                .build();

        healthRecordRepository.save(healthRecordMaks);

        // following animals
        userGalina.getFollowedAnimals().add(animalMishi);
        userGalina.getFollowedAnimals().add(animalBobi);
        userGalina.getFollowedAnimals().add(animalMaks);
        userRepository.save(userGalina);

        userRostislav.getFollowedAnimals().add(animalMishi);
        userRostislav.getFollowedAnimals().add(animalBobi);
        userRostislav.getFollowedAnimals().add(animalMaks);
        userRepository.save(userRostislav);

        userNikolay.getFollowedAnimals().add(animalMishi);
        userNikolay.getFollowedAnimals().add(animalBobi);
        userNikolay.getFollowedAnimals().add(animalMaks);
        userRepository.save(userNikolay);
    }
}
