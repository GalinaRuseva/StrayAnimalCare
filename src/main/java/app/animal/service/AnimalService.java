package app.animal.service;

import app.animal.model.Animal;
import app.animal.model.Status;
import app.animal.repository.AnimalRepository;
import app.exception.DomainException;
import app.location.model.Location;
import app.user.model.User;
import app.web.dto.AnimalEditRequest;
import app.web.dto.AnimalRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal createAnimal(AnimalRequest animalRequest, User user) {

        Location location = Location.builder()
                .country(animalRequest.getCountry())
                .city(animalRequest.getCity())
                .neighborhood(animalRequest.getNeighborhood())
                .build();

        Animal animal = Animal.builder()
                .type(animalRequest.getType())
                .name(animalRequest.getName())
                .estimateDateOfBirth(animalRequest.getEstimateDateOfBirth())
                .gender(animalRequest.getGender())
                .isNeutered(animalRequest.isNeutered())
                .status(Status.STRAY)
                .information(animalRequest.getInformation())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .location(location)
                .addedBy(user)
                .build();

        animalRepository.save(animal);

        return animal;
    }

    public Animal getById(UUID id) {

        return animalRepository.findById(id).orElseThrow(() -> new DomainException("Animal with id [%s] does not exist.".formatted(id)));
    }

    public List<Animal> getAllSystemAnimals() {

        return animalRepository.findAll();
    }

    public void deleteAnimalById(UUID id) {

        animalRepository.deleteById(id);
    }


    public void editAnimalDetails(UUID id, @Valid AnimalEditRequest animalEditRequest) {

        Animal animal = getById(id);

        Location location = Location.builder()
                .country(animalEditRequest.getCountry())
                .city(animalEditRequest.getCity())
                .neighborhood(animalEditRequest.getNeighborhood())
                .build();

        animal.setType(animalEditRequest.getType());
        animal.setName(animalEditRequest.getName());
        animal.setEstimateDateOfBirth(animalEditRequest.getEstimateDateOfBirth());
        animal.setGender(animalEditRequest.getGender());
        animal.setNeutered(animalEditRequest.isNeutered());
        animal.setStatus(animalEditRequest.getStatus());
        animal.setInformation(animalEditRequest.getInformation());
        animal.setUpdatedOn(LocalDateTime.now());
        animal.setLocation(location);

        animalRepository.save(animal);
    }
}
