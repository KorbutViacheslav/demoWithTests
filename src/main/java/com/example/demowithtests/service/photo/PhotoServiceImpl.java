package com.example.demowithtests.service.photo;

import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;

    @SneakyThrows
    @Override
    public Photo savePhoto(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileNotFoundException();
        }
        Photo photo = new Photo();
        photo.setName(StringUtils.cleanPath(file.getOriginalFilename()));
        photo.setData(file.getBytes());
        return photoRepository.save(photo);
    }

    @Override
    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Photo by id: " + id + " not found in database!"));
    }

    @Override
    @Transactional
    public Photo getPhotoByName(String name) {
        return photoRepository
                .findPhotoByName(name)
                .orElseThrow(() -> new NotFoundException("Photo by name: " + name + " not found in database!"));

    }

    @Override
    public String getName(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Photo not found")).getName();
    }

    @Override
    public void removePhotoById(Long id) {
        presetPhoto(id);
        photoRepository.deleteById(id);
    }

    @Override
    public List<Photo> getListPhoto() {
        List<Photo> photos = photoRepository.findAll();
        if (photos.isEmpty()) {
            throw new NotFoundException("List of photos is empty!");
        }
        return photos;
    }

    private boolean presetPhoto(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Passport is absent in database")) != null;
    }

}