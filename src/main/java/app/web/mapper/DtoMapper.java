package app.web.mapper;

import app.animal.model.Animal;
import app.user.model.User;
import app.web.dto.AnimalEditRequest;
import app.web.dto.UserEditRequest;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class DtoMapper {

    public static UserEditRequest mapUserToUserEditRequest(User user) {

        return UserEditRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .country(user.getLocation().getCountry())
                .city(user.getLocation().getCity())
                .neighborhood(user.getLocation().getNeighborhood())
                .build();
    }

    public static AnimalEditRequest mapAnimalToAnimalEditRequest(Animal animal) {

        return AnimalEditRequest.builder()
                .type(animal.getType())
                .name(animal.getName())
                .estimateDateOfBirth(animal.getEstimateDateOfBirth())
                .gender(animal.getGender())
                .neutered(animal.isNeutered())
                .status(animal.getStatus())
                .information(animal.getInformation())
                .country(animal.getLocation().getCountry())
                .city(animal.getLocation().getCity())
                .neighborhood(animal.getLocation().getNeighborhood())
                .build();
    }
}
