package com.example.demowithtests.service;

import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Photo savePhoto(String name, byte[] data) {
        Photo photo = new Photo();
        photo.setName(name);
        photo.setData(data);
        return photoRepository.save(photo);
    }

    public Optional<Photo> getPhotoById(Long id) {
        return photoRepository.findById(id);
    }

}