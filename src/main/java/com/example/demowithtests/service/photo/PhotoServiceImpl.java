package com.example.demowithtests.service.photo;

import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.repository.PhotoRepository;
import com.example.demowithtests.util.exception.photo.PhotoNoOneFindException;
import com.example.demowithtests.util.exception.photo.PhotoNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                .orElseThrow(PhotoNotFoundException::new);
    }

    @Override
    @Transactional
    public Photo getPhotoByName(String name) {
        return photoRepository
                .findPhotoByName(name)
                .orElseThrow(PhotoNotFoundException::new);

    }

    @Override
    public String getName(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(PhotoNotFoundException::new)
                .getName();
    }

    @Override
    public void removePhotoById(Long id) {
        presetPhoto(id);
        photoRepository.deleteById(id);
    }

    @Override
    public Map<Long, String> getMapPhoto() {
        List<Photo> photos = photoRepository.findAll();
        if (photos.isEmpty()) {
            throw new PhotoNoOneFindException();
        }
        return photos.stream().collect(Collectors.toMap(Photo::getId, Photo::getName));
    }

    @Override
    public Long getPassportNumber(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(PhotoNotFoundException::new);
        if (photo.getPassport() == null) {
            throw new NotFoundException("The photo is not pasted in the passport!");
        }
        return photo.getPassport().getId();
    }

    private boolean presetPhoto(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(PhotoNotFoundException::new) != null;
    }
}