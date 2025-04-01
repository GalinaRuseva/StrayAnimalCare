package app.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

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

    @NotNull
    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotNull
    @NotBlank(message = "Neighborhood cannot be empty")
    private String neighborhood;
}
