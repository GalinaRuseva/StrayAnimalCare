package app.web.validators;

import app.web.dto.AnimalRequest;
import app.web.dto.SingleFileUploadRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileInputValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SingleFileUploadRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SingleFileUploadRequest singleFileUploadRequest = (SingleFileUploadRequest) target;
        MultipartFile file = singleFileUploadRequest.getFile();

        if (file == null || file.isEmpty()) {
            errors.rejectValue("file", "file.empty", "Please select file to upload.");
        }
        if (file != null && !file.isEmpty() && file.getSize() > 2 * 1024 * 1024) {
            errors.rejectValue("file", "file.size", "File size must be less than 2MB");
        }
        if (file != null && !file.isEmpty() && !file.getContentType().startsWith("image/")) {
            errors.rejectValue("file", "file.type", "Only images are allowed");
        }
    }
}
