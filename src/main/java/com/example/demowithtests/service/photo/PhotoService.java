package com.example.demowithtests.service.photo;

import com.example.demowithtests.domain.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {
    Photo savePhoto(MultipartFile file);

    Photo getPhotoById(Long id);

    Photo getPhotoByName(String name);

    String getName(Long id);

    void removePhotoById(Long id);

    List<Photo> getListPhoto();

}
