package app.picture.client;

import app.picture.dto.PictureUploadResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "picture-service", url = "${picture-svc.base-url}")
public interface PictureClient {

    // url - main ednpoint
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<PictureUploadResponse> pictureUpload(@RequestPart(name = "picture") MultipartFile picture);

    @GetMapping("/view/{pictureId:.+}")
    ResponseEntity<byte[]> getPicture(@PathVariable String pictureId);

    @DeleteMapping("/{pictureId:.+}")
    ResponseEntity<Void> deletePicture(@PathVariable String pictureId);

}
