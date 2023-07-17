package com.example.demowithtests.service.work_place;

import com.example.demowithtests.domain.WorkPlace;

import java.util.List;

public interface WorkPlaceService {

    WorkPlace create(WorkPlace workPlace);

    WorkPlace getWorkPlaceById(Long id);

    WorkPlace updateWorkPlaceById(Long id);

    void deleteWorkplaceById(Long id);

    List<WorkPlace> getAllWorkPlaces();

    List<WorkPlace> getAllFreeWorkPlaces();

    List<WorkPlace> getAllBusyWorkPlaces();
}
