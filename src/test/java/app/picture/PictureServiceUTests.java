package app.picture;

import app.exception.DomainException;
import app.picture.client.PictureClient;
import app.picture.dto.PictureUploadResponse;
import app.picture.model.Picture;
import app.picture.repository.PictureRepository;
import app.picture.service.PictureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PictureServiceUTests {

    @Mock
    private PictureRepository pictureRepository;
    @Mock
    private PictureClient pictureClient;

    @InjectMocks
    private PictureService pictureService;

    @Test
    void givenPicture_whenSave_thenSavePictureAndReturnSavedPicture() {

        // given
        Picture pictureToSave = Picture.builder()
                .id(UUID.randomUUID())
                .storedPictureId(UUID.randomUUID().toString())
                .build();

        Picture savedPicture = Picture.builder()
                .id(pictureToSave.getId())
                .storedPictureId(pictureToSave.getStoredPictureId())
                .build();

        when(pictureRepository.save(pictureToSave)).thenReturn(savedPicture);

        // when
        Picture result = pictureService.save(pictureToSave);

        // then
        verify(pictureRepository, times(1)).save(pictureToSave);
        assertEquals(savedPicture, result);
    }


    @Test
    void givenPictureId_whenDeleteById_thenDeletePictureWhenPictureExists() {
        // given
        UUID id = UUID.randomUUID();
        UUID storedPictureId = UUID.randomUUID();
        Picture picture = Picture.builder()
                .id(id)
                .storedPictureId(storedPictureId.toString())
                .build();

        when(pictureRepository.findById(id)).thenReturn(Optional.of(picture));

        // then
        pictureService.deleteById(id);

        // when
        verify(pictureRepository, times(1)).deleteById(id);
        verify(pictureClient, times(1)).deletePicture(storedPictureId.toString());
    }

    @Test
    void givenNotExistingPictureId_whenDeleteById_thenPictureIsNotDeleted() {

        // given
        UUID id = UUID.randomUUID();

        when(pictureRepository.findById(id)).thenReturn(Optional.empty());

        // when
        pictureService.deleteById(id);

        // then
        verify(pictureRepository, times(1)).findById(id);
        verify(pictureRepository, never()).deleteById(id);
        verify(pictureClient, times(1)).deletePicture(id.toString());
    }

    @Test
    void givenFile_whenUploadPicture_thenReturnUploadedIdWhenUploadIsSuccessful() {

        // given
        MultipartFile file = new MockMultipartFile("file", "file.jpg", "image/jpeg", "file data".getBytes());
        PictureUploadResponse uploadResponse = PictureUploadResponse.builder()
                .id("uploadedId")
                .build();

        ResponseEntity<PictureUploadResponse> responseEntity = new ResponseEntity<>(uploadResponse, HttpStatus.OK);

        when(pictureClient.pictureUpload(file)).thenReturn(responseEntity);

        // when
        String uploadedId = pictureService.uploadPicture(file);

        // then
        assertEquals("uploadedId", uploadedId);
        verify(pictureClient, times(1)).pictureUpload(file);
    }

    @Test
    void givenFile_whenUploadPicture_thenExceptionIsThrown() {
        // given
        MultipartFile file = new MockMultipartFile("file", "file.jpg", "image/jpeg", "file data".getBytes());
        ResponseEntity<PictureUploadResponse> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(pictureClient.pictureUpload(file)).thenReturn(responseEntity);

        // when/then
        assertThrows(DomainException.class, () -> pictureService.uploadPicture(file));
        verify(pictureClient, times(1)).pictureUpload(file);
    }

    @Test
    void givenEmptyFile_whenUploadPicture_thenExceptionIsThrown() {
        // GIVEN
        MultipartFile file = new MockMultipartFile("file", "", "", new byte[0]);

        // WHEN/THEN
        assertThrows(DomainException.class, () -> pictureService.uploadPicture(file));
        verify(pictureClient, never()).pictureUpload(any(MultipartFile.class));
    }


}
