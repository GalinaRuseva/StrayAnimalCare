package app.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimalEditFilesUploadRequest {

//    @NotNull(message = "Please select file to upload.")
//    @Size(min = 1, message = "At least one picture is required")
    @NotEmpty(message = "Please select file to upload.")
    private MultipartFile[] animalPictures;
}
