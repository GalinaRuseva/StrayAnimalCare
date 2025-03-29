package app;


import app.animal.model.Animal;
import app.animal.model.Gender;
import app.animal.model.Type;
import app.animal.repository.AnimalRepository;
import app.animal.service.AnimalService;
import app.picture.repository.PictureRepository;
import app.picture.service.PictureService;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.AnimalRequest;
import app.web.dto.RegisterRequest;
import app.web.dto.SingleFileUploadRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class UploadPicturesITest {

    @Autowired
    private AnimalService animalService;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @MockitoBean
    //@Autowired
    private PictureService pictureService;

    @Autowired
    private UserService userService;

    @Test
    void givenActiveUser_whenUploadProfilePicture_thenUserIsSavedWithProfilePicture() {
        // Given
        MultipartFile profilePicture = new MockMultipartFile("profilePicture", "profile.jpg", "image/jpeg", "profile data".getBytes());
        //MultipartFile[] animalPictures = {new MockMultipartFile("picture1", "pic1.jpg", "image/jpeg", "pic1 data".getBytes())};

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Valentin")
                .password("123456")
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Izgrev")
                .build();

        User user = userService.register(registerRequest);

        SingleFileUploadRequest singleFileUploadRequest = SingleFileUploadRequest.builder()
                .file(profilePicture)
                .build();

        UUID pictureId = UUID.randomUUID();

        when(pictureService.uploadPicture(any(MultipartFile.class))).thenReturn(pictureId.toString());

        // When
        User userWithPic = userService.saveProfilePicture(user.getId(), singleFileUploadRequest);
//        userService.saveProfilePicture(user, singleFileUploadRequest);
        // Then
//        assertEquals(pictureId.toString(), user.getProfilePicture());

        assertNotNull(userWithPic.getProfilePicture());
    }


    //@Test
    //    void givenAnimalRequest_whenCreateAnimal_thenAnimalIsSavedWithPictures() {
    //        // Given
    //        MultipartFile profilePicture = new MockMultipartFile("profile", "profile.jpg", "image/jpeg", "profile data".getBytes());
    //        MultipartFile[] animalPictures = {new MockMultipartFile("picture1", "pic1.jpg", "image/jpeg", "pic1 data".getBytes())};
    //
    //        AnimalRequest animalRequest = AnimalRequest.builder()
    //                .type(Type.DOG)
    //                .name("Maks")
    //                .estimateDateOfBirth(LocalDate.now().minusYears(2))
    //                .gender(Gender.MALE)
    //                .neutered(true)
    //                .information("Big and friendly black dog")
    //                .country("Bulgaria")
    //                .city("Sofia")
    //                .neighborhood("Izgrev")
    //                //.animalProfilePicture(profilePicture)
    //                //.animalPictures(animalPictures)
    //                .build();
    //
    //        RegisterRequest registerRequest = RegisterRequest.builder()
    //                .username("Valentin")
    //                .password("123456")
    //                .country("Bulgaria")
    //                .city("Sofia")
    //                .neighborhood("Izgrev")
    //                .build();
    //
    //        User user = userService.register(registerRequest);
    //
    //        when(pictureService.uploadPicture(any(MultipartFile.class))).thenReturn(UUID.randomUUID().toString());
    ////        when(pictureService.uploadPicture(profilePicture)).thenReturn(UUID.randomUUID().toString());
    ////        when(pictureService.uploadPicture(any(MultipartFile.class))).thenReturn(UUID.randomUUID().toString());
    //
    //        // When
    //        Animal animal = animalService.createAnimal(animalRequest, user);
    //
    //        // Then
    //        assertNotNull(animal);
    //        assertNotNull(animal.getId());
    //        assertEquals(animalRequest.getName(), animal.getName());
    //        assertEquals(animalRequest.getType(), animal.getType());
    //
    //        Animal retrievedAnimal = animalRepository.findById(animal.getId()).orElse(null);
    //        assertNotNull(retrievedAnimal);
    //        assertEquals(animal.getId(), retrievedAnimal.getId());
    //
    ////        Picture savedPicture = pictureRepository.findByAnimal(retrievedAnimal).get(0);
    ////        assertNotNull(savedPicture);
    ////        assertNotNull(savedPicture.getStoredPictureId());
    //
    ////        userRepository.delete(user);
    ////        pictureRepository.delete(savedPicture);
    ////        animalRepository.delete(retrievedAnimal);
    //    }


}
