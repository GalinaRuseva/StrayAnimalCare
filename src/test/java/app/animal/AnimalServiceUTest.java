package app.animal;

import app.animal.model.Animal;
import app.animal.repository.AnimalRepository;
import app.animal.service.AnimalService;
import app.exception.DomainException;
import app.location.model.Location;
import app.picture.client.PictureClient;
import app.picture.service.PictureService;
import app.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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




}
