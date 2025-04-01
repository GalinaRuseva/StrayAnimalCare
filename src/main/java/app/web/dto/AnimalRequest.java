package app.web.dto;

import app.animal.model.Gender;
import app.animal.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimalRequest {

    private UUID id;

    @NotNull(message = "You must select an animal type!")
    private Type type;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 20, message = "Name can't have more than 20 symbols")
    private String name;

    @NotNull(message = "Must not be null")
    private LocalDate estimateDateOfBirth;

    @NotNull(message = "You must select a gender!")
    private Gender gender;

    private boolean neutered;

    @Size(max = 80, message = "Information can't have more than 80 symbols")
    private String information;

    @NotBlank(message = "Country cannot be empty")
    private String country;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "Neighborhood cannot be empty")
    private String neighborhood;

    private MultipartFile animalProfilePicture;

    private MultipartFile[] animalPictures;
}
