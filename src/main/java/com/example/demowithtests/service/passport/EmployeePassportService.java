package com.example.demowithtests.service.passport;

import com.example.demowithtests.domain.EmployeePassport;

import java.util.List;
import java.util.Optional;

public interface EmployeePassportService {
    EmployeePassport create(EmployeePassport employeePassport);

    List<EmployeePassport> getAll();

    EmployeePassport update(Long id, EmployeePassport employeePassport);

    void remove(Long id);

    Optional<EmployeePassport> getPassportById(Long id);

    List<EmployeePassport> getAllHanded();

    List<EmployeePassport> getAllNotHanded();

    EmployeePassport addPhoto(Long id, String link);

    EmployeePassport handPassport(Long id, String link);
}
