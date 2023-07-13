package com.example.demowithtests.service.passport;

import com.example.demowithtests.domain.EmployeePassport;

import java.util.List;
import java.util.Optional;

public interface EmployeePassportService {
    EmployeePassport create(EmployeePassport employeePassport);

    List<EmployeePassport> getAll();

    EmployeePassport update(Long id);

    void remove(Long id);

    EmployeePassport getPassportById(Long id);

    List<EmployeePassport> getAllHanded();

    List<EmployeePassport> getAllNotHanded();

    public EmployeePassport pastePhoto(Long passportId, Long photoId);
}
