package com.example.demowithtests.web;

import com.example.demowithtests.service.photo.PhotoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/photos")
@Tag(name = "Photo", description = "Photo API")
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<String> savePhoto(@RequestParam("file") MultipartFile file) {
        try {
            photoService.savePhoto(file);
            return ResponseEntity.ok()
                    .body(String.format("File uploaded successfully: %s",
                            file.getOriginalFilename()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(String.format("Could not upload the file: %s",
                            file.getOriginalFilename()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPhotoById(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(photoService.getPhotoById(id).getData());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<byte[]> getPhotoByName(@PathVariable("name") String name) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(photoService.getPhotoByName(name).getData());
    }

    @GetMapping("/namE/{id}")
    public ResponseEntity<String> getPhotoNameById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(photoService.getName(id));
    }

    @GetMapping("/list")
    public ResponseEntity<Map<Long, String>> getAllNamePhotos() {
        return ResponseEntity.ok().body(photoService.getMapPhoto());
    }

    @GetMapping("/pas/{id}")
    public ResponseEntity<String> getPassportNumber(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(String.format("Photo by id: %s Have id of passport: %s",
                        id,
                        photoService.getPassportNumber(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePhotoById(@PathVariable Long id) {
        String massage = String.format("Successfully! Photo by id: %s was deleted!", id);
        photoService.removePhotoById(id);
        return ResponseEntity.ok().body(massage);

    }
}
