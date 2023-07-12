package com.example.demowithtests.service;

import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Photo savePhoto(MultipartFile file) throws IOException {
        Photo photo = new Photo();
        photo.setName(StringUtils.cleanPath(file.getOriginalFilename()));
        photo.setData(file.getBytes());
        return photoRepository.save(photo);
    }

    public Optional<Photo> getPhotoById(Long id) {
        return photoRepository.findById(id);
    }

}