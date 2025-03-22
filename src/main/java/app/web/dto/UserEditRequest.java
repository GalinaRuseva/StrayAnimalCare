package app.web.dto;

import app.picture.model.Picture;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class UserEditRequest {

    @Size(max = 20, message = "First name can't have more than 15 symbols")
    private String firstName;

    @Size(max = 20, message = "Last name can't have more than 15 symbols")
    private String lastName;

    @Email(message = "Requires correct email format")
    private String email;

    private String phoneNumber;

    @NotBlank(message = "Country cannot be empty")
    private String country;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "Neighborhood cannot be empty")
    private String neighborhood;

    private MultipartFile profilePictureFile;
}
