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

        // Then
        assertNotNull(userWithPic.getProfilePicture());
    }

}
