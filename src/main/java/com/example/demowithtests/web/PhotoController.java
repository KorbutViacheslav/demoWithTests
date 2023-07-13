package com.example.demowithtests.web;

import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.service.PhotoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/photos")
@Tag(name = "Photo", description = "Photo API")
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        try {
            photoService.savePhoto(file);
            return ResponseEntity.status(HttpStatus.OK).
                    body(String.format("File uploaded successfully: %s", file.getOriginalFilename()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(String.format("Could not upload the file: %s", file.getOriginalFilename()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable("id") Long id) {
        Optional<Photo> photo = photoService.getPhotoById(id);
        return photo.map(value -> ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(value.getData())).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<byte[]>  getImageByName(@PathVariable("name") String name){
        byte[] image = photoService.getPhotoByName(name);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }
    @GetMapping("/namE/{id}")
    public ResponseEntity<String> getName(@PathVariable Long id){
        return ResponseEntity.ok().body(photoService.getName(id));
    }

}
