package com.example.demowithtests.web;
import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhoto(@RequestParam("name") String name,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        byte[] data = file.getBytes();
        Photo photo = photoService.savePhoto(name, data);
        return ResponseEntity.ok(photo);
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
