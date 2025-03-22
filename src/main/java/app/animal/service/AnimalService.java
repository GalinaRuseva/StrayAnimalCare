package app.animal.service;

import app.animal.model.Animal;
import app.animal.model.Status;
import app.animal.repository.AnimalRepository;
import app.exception.DomainException;
import app.location.model.Location;
import app.picture.client.PictureClient;
import app.picture.dto.PictureUploadResponse;
import app.picture.model.Picture;
import app.picture.service.PictureService;
import app.user.model.User;
import app.web.dto.AnimalEditRequest;
import app.web.dto.AnimalRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final PictureService pictureService;
    // TODO: maybe move pictureCLient to pictureService
    private final PictureClient pictureClient;

    @Autowired
    public AnimalService(AnimalRepository animalRepository, PictureService pictureService, PictureClient pictureClient) {
        this.animalRepository = animalRepository;
        this.pictureService = pictureService;
        this.pictureClient = pictureClient;
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
//                .profilePicture(animalRequest.getProfilePicture())
                .information(animalRequest.getInformation())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .location(location)
                .addedBy(user)
                .build();

        if (!animalRequest.getAnimalProfilePicture().isEmpty()) {
            ResponseEntity<PictureUploadResponse> pictureUpload = pictureClient.pictureUpload(animalRequest.getAnimalProfilePicture());
            if (pictureUpload.getStatusCode().is2xxSuccessful()) {
                animal.setProfilePicture(pictureUpload.getBody().getId());
            }
        }

        animalRepository.save(animal);
        savePictureToAnimal(animal.getId(), animalRequest.getAnimalPictures());
        return animal;
    }

    public Animal getById(UUID id) {

        return animalRepository.findById(id).orElseThrow(() -> new DomainException("Animal with id [%s] does not exist.".formatted(id)));
    }

    public List<Animal> getAllSystemAnimals() {

        return animalRepository.findAll();
    }

//    public void deleteAnimalById(UUID id) {
//        animalRepository.deleteById(id);
//    }

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
//        animal.setProfilePicture(animalEditRequest.getProfilePicture());
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
        Arrays.stream(files).forEach(animalPicture -> {
            if (!animalPicture.isEmpty()) {
                ResponseEntity<PictureUploadResponse> pictureUpload = pictureClient.pictureUpload(animalPicture);
                if (pictureUpload.getStatusCode().is2xxSuccessful()) {
                    Picture build = Picture.builder()
                            .animal(animal)
                            .storedPictureId(pictureUpload.getBody().getId()).build();
                    pictureService.save(build);
                }
            }
        });
    }

    public void deletePictureFromAnimal(UUID pictureId) {
        this.pictureService.deleteById(pictureId);
    }

    public Animal resetProfilePicture(UUID id) {
        Animal animal = getById(id);
        animal.setProfilePicture(null);
        return this.animalRepository.save(animal);
    }

    public Animal saveProfilePicture(UUID id, MultipartFile file) {
        Animal animal = getById(id);
        if (!file.isEmpty()) {
            ResponseEntity<PictureUploadResponse> pictureUpload = pictureClient.pictureUpload(file);
            if (pictureUpload.getStatusCode().is2xxSuccessful()) {
                animal.setProfilePicture(pictureUpload.getBody().getId());
            }
        }
        return this.animalRepository.save(animal);
    }
}
