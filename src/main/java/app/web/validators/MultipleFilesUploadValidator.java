package app.web.validators;

import app.web.dto.AnimalEditFilesUploadRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipleFilesUploadValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AnimalEditFilesUploadRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AnimalEditFilesUploadRequest request = (AnimalEditFilesUploadRequest) target;
        MultipartFile[] animalPictures = request.getAnimalPictures();

        if (animalPictures == null || animalPictures.length == 0 || animalPictures[0].isEmpty()) {
            errors.rejectValue("animalPictures", "animalPictures.empty", "Please select to upload at least one picture.");
            return;
        }

        for (int i = 0; i < animalPictures.length; i++) {
            MultipartFile file = animalPictures[i];
            if (file != null && !file.isEmpty()) {
                if (file.getSize() > 2 * 1024 * 1024) { // 2MB limit.
                    errors.rejectValue("animalPictures", "animalPictures[" + i + "].size", "File " + (i + 1) + " size must be less than 2MB");
                }
                if (!file.getContentType().startsWith("image/")) {
                    errors.rejectValue("animalPictures", "animalPictures[" + i + "].type", "File " + (i + 1) + " must be an image");
                }
            }
        }
    }
}
