package app.picture.service;

import app.exception.DomainException;
import app.picture.client.PictureClient;
import app.picture.dto.PictureUploadResponse;
import app.picture.model.Picture;
import app.picture.repository.PictureRepository;
import app.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
public class PictureService {

    private final PictureRepository pictureRepository;
    private final PictureClient pictureClient;

    @Autowired
    public PictureService(PictureRepository pictureRepository, PictureClient pictureClient) {
        this.pictureRepository = pictureRepository;
        this.pictureClient = pictureClient;
    }

    public Picture save(Picture picture) {
        return pictureRepository.save(picture);
    }

    public void deleteById(UUID id) {
        this.pictureRepository.deleteById(id);
        pictureClient.deletePicture(id.toString());
    }

//    public String saveProfilePicture(MultipartFile file) {
//        if (!file.isEmpty()) {
//            ResponseEntity<PictureUploadResponse> pictureUpload = pictureClient.pictureUpload(file);
//            if (pictureUpload.getStatusCode().is2xxSuccessful()) {
//                return pictureUpload.getBody().getId();
//            }
//        }
//        throw new DomainException("Can't upload picture file");
//    }
}
