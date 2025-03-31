package app.animal.service;

import app.animal.model.Animal;
import app.animal.model.Status;
import app.animal.repository.AnimalRepository;
import app.exception.DomainException;
import app.location.model.Location;
import app.picture.model.Picture;
import app.picture.service.PictureService;
import app.user.model.User;
import app.web.dto.AnimalEditRequest;
import app.web.dto.AnimalRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final PictureService pictureService;

    @Autowired
    public AnimalService(AnimalRepository animalRepository, PictureService pictureService) {
        this.animalRepository = animalRepository;
        this.pictureService = pictureService;
    }


    public Animal createAnimal(AnimalRequest animalRequest, User user) {

        Location location = Location.builder()
                .country(StringUtils.trim(animalRequest.getCountry()))
                .city(StringUtils.trim(animalRequest.getCity()))
                .neighborhood(StringUtils.trim(animalRequest.getNeighborhood()))
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

//        animalRepository.save(animal);

        if (animalRequest.getAnimalProfilePicture() != null && !animalRequest.getAnimalProfilePicture().isEmpty()) {
            String storedPictureId = pictureService.uploadPicture(animalRequest.getAnimalProfilePicture());
            animal.setProfilePicture(storedPictureId);
        }

        animalRepository.save(animal);

        if (animalRequest.getAnimalPictures() != null && animalRequest.getAnimalPictures().length > 0) {
            savePictureToAnimal(animal.getId(), animalRequest.getAnimalPictures());
        }

//        animalRepository.save(animal);
//        savePictureToAnimal(animal.getId(), animalRequest.getAnimalPictures());
        return animal;
    }

    public Animal getById(UUID id) {

        return animalRepository.findById(id).orElseThrow(() -> new DomainException("Animal with id [%s] does not exist.".formatted(id)));
    }

    public List<Animal> getAllSystemAnimals() {

        return animalRepository.findAll();
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

    public List<Animal> getAnimalsNearUserLocation(User user) {

        String userCountry = user.getLocation().getCountry();
        String userCity = user.getLocation().getCity();
        String userNeighborhood = user.getLocation().getNeighborhood();

        return animalRepository.findByAnimalLocation(userCountry, userCity, userNeighborhood);
    }

    public void savePictureToAnimal(UUID id, MultipartFile[] files) {

        Animal animal = getById(id);

        if(files != null && files.length > 0) {

            Arrays.stream(files).forEach(animalPicture -> {
                if (!animalPicture.isEmpty()) {

                    String storedPictureId = pictureService.uploadPicture(animalPicture);
                    Picture build = Picture.builder()
                            .animal(animal)
                            .uploadDate(LocalDateTime.now())
                            .storedPictureId(storedPictureId).build();
                    pictureService.save(build);
                }
            });
        }

    }

    public void deletePictureFromAnimal(UUID pictureId) {
        this.pictureService.deleteById(pictureId);
    }

    public Animal deleteProfilePicture(UUID id, UUID pictureId) {
        Animal animal = getById(id);
        pictureService.deleteById(pictureId);
        animal.setProfilePicture(null);
        return this.animalRepository.save(animal);
    }

    public Animal saveProfilePicture(UUID id, MultipartFile file) {
        Animal animal = getById(id);
        String storedPictureId = pictureService.uploadPicture(file);
        animal.setProfilePicture(storedPictureId);
        return this.animalRepository.save(animal);
    }
}
