package com.example.demowithtests.service.work_place;

import com.example.demowithtests.domain.WorkPlace;
import com.example.demowithtests.repository.WorkPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

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
        return workPlaceRepository.findById(id).orElseThrow(()->new NotFoundException("Work place is absent!"));
    }

    @Override
    public WorkPlace updateWorkPlaceById(Long id) {
        return null;
    }

    @Override
    public void deleteWorkplaceById(Long id) {
        WorkPlace workPlace = workPlaceRepository.findById(id).orElseThrow();
        workPlaceRepository.deleteById(workPlace.getId());
    }

    @Override
    public List<WorkPlace> getAllWorkPlaces() {
        List<WorkPlace> list = workPlaceRepository.findAll();
        if (list.isEmpty()) {
            throw new NotFoundException("No one work places found!");
        } else {
            return list;
        }
    }

    @Override
    public List<WorkPlace> getAllFreeWorkPlaces() {
        List<WorkPlace> list = workPlaceRepository.findAllByIsActiveTrue();
        if (list.isEmpty()) {
            throw new NotFoundException("No one work places found!");
        } else {
            return list;
        }
    }

    @Override
    public List<WorkPlace> getAllBusyWorkPlaces() {
        List<WorkPlace> list = workPlaceRepository.findAllByIsActiveFalse();
        if (list.isEmpty()) {
            throw new NotFoundException("No one work places found!");
        } else {
            return list;
        }
    }
}
