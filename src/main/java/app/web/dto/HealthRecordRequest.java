package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthRecordRequest {

    @NotBlank(message = "Description cannot be empty.")
    private String description;

    @NotBlank(message = "Veterinary information cannot be empty")
    private String veterinaryInformation;
}
