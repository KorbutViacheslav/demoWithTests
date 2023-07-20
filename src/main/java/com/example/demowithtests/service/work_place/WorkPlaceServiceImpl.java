package com.example.demowithtests.service.work_place;

import com.example.demowithtests.domain.WorkPlace;
import com.example.demowithtests.repository.WorkPlaceRepository;
import com.example.demowithtests.util.exception.workplace.WorkPlaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkPlaceServiceImpl implements WorkPlaceService {

    private final WorkPlaceRepository workPlaceRepository;

    @Override
    public WorkPlace create(WorkPlace workPlace) {
        return workPlaceRepository.save(workPlace);
    }

    @Override
    public WorkPlace getWorkPlaceById(Long id) {
        return workPlaceRepository.findById(id).orElseThrow(WorkPlaceNotFoundException::new);
    }

    @Override
    public void deleteWorkplaceById(Long id) {
        WorkPlace workPlace = workPlaceRepository.findById(id).orElseThrow(WorkPlaceNotFoundException::new);
        workPlaceRepository.deleteById(workPlace.getId());
    }

    @Override
    public List<WorkPlace> getAllWorkPlaces() {
        List<WorkPlace> list = workPlaceRepository.findAll();
        if (list.isEmpty()) {
            throw new WorkPlaceNotFoundException();
        } else {
            return list;
        }
    }

    @Override
    public List<WorkPlace> getAllFreeWorkPlaces() {
        List<WorkPlace> list = workPlaceRepository.findAllByIsFreeTrue();
        if (list.isEmpty()) {
            throw new WorkPlaceNotFoundException();
        } else {
            return list;
        }
    }

    @Override
    public List<WorkPlace> getAllBusyWorkPlaces() {
        List<WorkPlace> list = workPlaceRepository.findAllByIsFreeFalse();
        if (list.isEmpty()) {
            throw new WorkPlaceNotFoundException();
        } else {
            return list;
        }
    }
}
