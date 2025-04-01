package app.web.dto;

import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "Please select file to upload.")
    private MultipartFile[] animalPictures;
}
