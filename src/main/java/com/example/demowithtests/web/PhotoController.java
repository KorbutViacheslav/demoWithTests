package com.example.demowithtests.web;

import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.service.PhotoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/photos")
@Tag(name = "Photo", description = "Photo API")
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

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
    public ResponseEntity<byte[]> downloadPhoto(@PathVariable("id") Long id) {
        Optional<Photo> photo = photoService.getPhotoById(id);
        if (photo.isPresent()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(photo.get().getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
