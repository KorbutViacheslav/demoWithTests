package com.example.demowithtests.util.config.mapstruct;

import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.dto.photo.PhotoRec;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface PhotoMapper {
    PhotoMapper INSTANCE = Mappers.getMapper(PhotoMapper.class);


    Photo toPhoto(PhotoRec photoRec);


    PhotoRec toPhotoRec(Photo photo);
}
