package app.animal;

import app.animal.model.Animal;
import app.animal.model.Gender;
import app.animal.model.Status;
import app.animal.model.Type;
import app.animal.repository.AnimalRepository;
import app.animal.service.AnimalService;
import app.exception.DomainException;
import app.location.model.Location;
import app.picture.client.PictureClient;
import app.picture.model.Picture;
import app.picture.service.PictureService;
import app.user.model.User;
import app.web.dto.AnimalEditRequest;
import app.web.dto.SingleFileUploadRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceUTest {

    @Mock
    private AnimalRepository animalRepository;
    @Mock
    private PictureService pictureService;
    @Mock
    private PictureClient pictureClient;

    @InjectMocks
    private AnimalService animalService;


    @Test
    void givenExistingAnimalsInDataBase_whenGetAllAnimals_thenReturnThemAll() {

        //given
        List<Animal> animalsList = List.of(new Animal(), new Animal(), new Animal());
        when(animalRepository.findAll()).thenReturn(animalsList);

        //when
        List<Animal> allSystemAnimals = animalService.getAllSystemAnimals();

        //then
        assertThat(allSystemAnimals).hasSize(3);
    }

    @Test
    void givenNotExistingAnimal_whenGetById_thenExceptionIsThrown() {

        //given
        UUID animalId = UUID.randomUUID();
        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(DomainException.class, () -> animalService.getById(animalId));
    }

    @Test
    void givenExistingAnimal_whenGetById_thenReturnCorrectAnimal() {

        //given
        Animal animal = Animal.builder()
                .id(UUID.randomUUID())
                .build();
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.of(animal));

        //when
        Animal animalFromDatabase = animalService.getById(animal.getId());

        //then
        assertEquals(animal.getId(), animalFromDatabase.getId());
    }

    @Test
    void givenSameUserLocationLikeAnimalsLocation_whenGetAnimalsNearUserLocation_thenReturnAnimals() {

        //given
        Location location = Location.builder()
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Izgrev")
                .build();

        User user = User.builder()
                .location(location)
                .build();

        Animal animalFirst = Animal.builder()
                .location(location)
                .build();
        Animal animalSecond = Animal.builder()
                .location(location)
                .build();

        List<Animal> animals = new ArrayList<>();
        animals.add(animalFirst);
        animals.add(animalSecond);

        when(animalRepository.findByAnimalLocation(user.getLocation().getCountry(), user.getLocation().getCity(), user.getLocation().getNeighborhood())).thenReturn(animals);

        //when
        List<Animal> animalsNearUserLocation = animalService.getAnimalsNearUserLocation(user);

        //then
        assertEquals(animals, animalsNearUserLocation);
        assertThat(animalsNearUserLocation).hasSize(2);
    }

    @Test
    void givenDiffUserLocationFromAnimalsLocation_whenGetAnimalsNearUserLocation_thenReturnEmptyList() {

        //given
        Location animalLocation = Location.builder()
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Izgrev")
                .build();

        Location userLocation = Location.builder()
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Mladost")
                .build();

        User user = User.builder()
                .location(userLocation)
                .build();

        Animal animalFirst = Animal.builder()
                .location(animalLocation)
                .build();
        Animal animalSecond = Animal.builder()
                .location(animalLocation)
                .build();

        List<Animal> animals = new ArrayList<>();
        animals.add(animalFirst);
        animals.add(animalSecond);

        when(animalRepository.findByAnimalLocation(user.getLocation().getCountry(), user.getLocation().getCity(), user.getLocation().getNeighborhood())).thenReturn(new ArrayList<>());

        //when
        List<Animal> animalsNearUserLocation = animalService.getAnimalsNearUserLocation(user);

        //then
        assertThat(animalsNearUserLocation).hasSize(0);
    }

    @Test
    void givenAnimalIdAnimalEditRequest_whenEditAnimalDetails_thenEditAnimalProfile() {

        Location location = Location.builder()
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Borovo")
                .build();

        //given
        UUID animalId = UUID.randomUUID();

        Animal animal = Animal.builder()
                .id(animalId)
                .name("Koko")
                .type(Type.CAT)
                .isNeutered(false)
                .gender(Gender.MALE)
                .location(location)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        AnimalEditRequest animalEditRequest = AnimalEditRequest.builder()
                .type(Type.CAT)
                .name("Koko")
                .estimateDateOfBirth(LocalDate.now().minusYears(3))
                .gender(Gender.MALE)
                .neutered(true)
                .status(Status.ADOPTED)
                .information("Koko have new home")
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Borovo")
                .build();

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));

        //when
        animalService.editAnimalDetails(animalId, animalEditRequest);
        List<Animal> allSystemAnimals = animalService.getAllSystemAnimals();

        //then
        assertThat(animal.isNeutered()).isTrue();
        assertThat(animal.getStatus() == Status.ADOPTED).isTrue();
        verify(animalRepository, times(1)).save(animal);
    }

    @Test
    void givenPictureID_whenDeletePictureFromAnimal_shouldDeletePictureById() {
        // given
        UUID pictureId = UUID.randomUUID();

        // when
        animalService.deletePictureFromAnimal(pictureId);

        // then
        verify(pictureService, times(1)).deleteById(pictureId);
    }

    @Test
    void givenAnimalIdPictureId_whenDeleteProfilePicture_shouldDeletePictureAndSetProfilePictureToNull() {
        // given
        UUID animalId = UUID.randomUUID();
        UUID pictureId = UUID.randomUUID();
        Animal animal = Animal.builder().id(animalId).profilePicture("somePictureId").build();

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(animalRepository.save(any())).thenReturn(animal);

        // when
        Animal updatedAnimal = animalService.deleteProfilePicture(animalId, pictureId);

        // then
        verify(animalRepository, times(1)).save(animal);
        assertNull(updatedAnimal.getProfilePicture());
        assertEquals(animalId, updatedAnimal.getId());
    }

    @Test
    void givenAnimalIdPictureId_whenDeleteProfilePicture_thenExceptionIsThrown() {
        // given
        UUID animalId = UUID.randomUUID();
        UUID pictureId = UUID.randomUUID();

        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());

        // when/then
        assertThrows(DomainException.class, () -> animalService.deleteProfilePicture(animalId, pictureId));

        verify(pictureService, never()).deleteById(any());
        verify(animalRepository, never()).save(any());
    }

    @Test
    void givenAnimalIdAndSingleFileUploadRequest_whenSaveProfilePicture_shouldSavePictureAndSetProfilePicture() {
        // given
        UUID animalId = UUID.randomUUID();
        MultipartFile file = new MockMultipartFile("profile", "profile.jpg", "image/jpeg", "profile data".getBytes());

        String storedPictureId = UUID.randomUUID().toString();
        Animal animal = Animal.builder().id(animalId).build();

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(pictureService.uploadPicture(file)).thenReturn(storedPictureId);
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);

        // when
        Animal updatedAnimal = animalService.saveProfilePicture(animalId, file);

        // then
        verify(pictureService, times(1)).uploadPicture(file);
        verify(animalRepository, times(1)).save(animal);
        assertEquals(storedPictureId, updatedAnimal.getProfilePicture());
        assertEquals(animalId, updatedAnimal.getId());
    }

    @Test
    void givenMissingAnimalInDatabase_whenSaveProfilePicture_thenExceptionIsThrown() {
        // given
        UUID animalId = UUID.randomUUID();
        MultipartFile file = new MockMultipartFile("profile", "profile.jpg", "image/jpeg", "profile data".getBytes());

        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());

        // when/then
        assertThrows(DomainException.class, () -> animalService.saveProfilePicture(animalId, file));

        verify(pictureService, never()).uploadPicture(any(MultipartFile.class));
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    void givenPictures_whenSavePictureToAnimal_thenSavePicturesWhenAnimalExists() {

        // given
        UUID animalId = UUID.randomUUID();
        Animal animal = Animal.builder().id(animalId).build();
        MultipartFile[] files = new MultipartFile[]{
                new MockMultipartFile("file1", "file1.jpg", "image/jpeg", "file1".getBytes()),
                new MockMultipartFile("file2", "file2.png", "image/png", "file2".getBytes())
        };
        String storedPictureId = UUID.randomUUID().toString();

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(pictureService.uploadPicture(any())).thenReturn(storedPictureId);

        // when
        animalService.savePictureToAnimal(animalId, files);

        // then
        verify(animalRepository, times(1)).findById(animalId);
        verify(pictureService, times(2)).uploadPicture(any());
        verify(pictureService, times(2)).save(any());

        ArgumentCaptor<Picture> pictureCaptor = ArgumentCaptor.forClass(Picture.class);
        verify(pictureService, times(2)).save(pictureCaptor.capture());
        assertEquals(animal, pictureCaptor.getAllValues().get(0).getAnimal());
        assertEquals(storedPictureId, pictureCaptor.getAllValues().get(0).getStoredPictureId());
        assertNotNull(pictureCaptor.getAllValues().get(0).getUploadDate());
        assertEquals(animal, pictureCaptor.getAllValues().get(1).getAnimal());
        assertEquals(storedPictureId, pictureCaptor.getAllValues().get(1).getStoredPictureId());
        assertNotNull(pictureCaptor.getAllValues().get(1).getUploadDate());
    }

        @Test
    void givenMissingAnimalInDatabase_whenSavePictureToAnimal_thenExceptionIsThrow() {

        // given
        UUID animalId = UUID.randomUUID();
        MultipartFile[] files = new MultipartFile[]{
                new MockMultipartFile("file1", "file1.jpg", "image/jpeg", "file1".getBytes())
        };

        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());

        // WHEN/THEN
        assertThrows(DomainException.class, () -> animalService.savePictureToAnimal(animalId, files));

        verify(animalRepository, times(1)).findById(animalId);
        verify(pictureService, never()).uploadPicture(any());
        verify(pictureService, never()).save(any());
    }

    @Test
    void givenEmptyFiles_whenSavePictureToAnimal_thenNotSaveEmptyFiles() {

        // given
        UUID animalId = UUID.randomUUID();
        Animal animal = Animal.builder().id(animalId).build();
        MultipartFile[] files = new MultipartFile[]{
                new MockMultipartFile("file1", "file1.jpg", "image/jpeg", "file1".getBytes()),
                new MockMultipartFile("empty", "", "", "".getBytes())
        };
        String storedPictureId = UUID.randomUUID().toString();

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(pictureService.uploadPicture(any())).thenReturn(storedPictureId);

        // when
        animalService.savePictureToAnimal(animalId, files);

        // THEN
        verify(animalRepository, times(1)).findById(animalId);
        verify(pictureService, times(1)).uploadPicture(any());
        verify(pictureService, times(1)).save(any());
    }




}
