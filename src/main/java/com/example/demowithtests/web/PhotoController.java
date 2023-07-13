package com.example.demowithtests.web;

import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.service.photo.PhotoServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/photos")
@Tag(name = "Photo", description = "Photo API")
public class PhotoController {
    private final PhotoServiceImpl photoServiceImpl;

    @PostMapping
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        try {
            photoServiceImpl.savePhoto(file);
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
    public ResponseEntity<byte[]> getPhoto(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(photoServiceImpl.getPhotoById(id).getData());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(photoServiceImpl.getPhotoByName(name).getData());
    }

    @GetMapping("/namE/{id}")
    public ResponseEntity<String> getName(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(photoServiceImpl.getName(id));
    }

    /*    @GetMapping("/list")
        public ResponseEntity<List<byte[]>> getAll(){
            List<Photo> list=photoServiceImpl.getListPhoto();
            List<byte[]> b = list.stream().map(Photo::getData).toList();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(b);
        }*/
    @GetMapping("/list")
    public ResponseEntity<List<byte[]>> getAllPhotos() {
        List<Photo> photoList = photoServiceImpl.getListPhoto();
        List<byte[]> imageBytesList = photoList.stream()
                .map(Photo::getData)
                .collect(Collectors.toList());
        imageBytesList.forEach(e->{
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(e);
        });
    }

}
