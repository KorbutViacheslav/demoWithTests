package com.example.demowithtests.repository;

import com.example.demowithtests.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findPhotoByName(String name);
}
