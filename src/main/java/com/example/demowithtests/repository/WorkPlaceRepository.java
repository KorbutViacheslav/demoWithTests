package com.example.demowithtests.repository;

import com.example.demowithtests.domain.WorkPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkPlaceRepository extends JpaRepository<WorkPlace, Long> {

    List<WorkPlace> findAllByIsFreeTrue();

    List<WorkPlace> findAllByIsFreeFalse();
}
