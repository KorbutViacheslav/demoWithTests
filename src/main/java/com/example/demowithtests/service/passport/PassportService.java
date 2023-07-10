package com.example.demowithtests.service.passport;

import com.example.demowithtests.domain.EmployeePassport;
import com.example.demowithtests.repository.EmployeePassportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassportService implements EmployeePassportService {

    private final EmployeePassportRepository passportRepository;

    @Override
    public EmployeePassport create(EmployeePassport employeePassport) {
        return passportRepository.save(employeePassport);
    }

    @Override
    public List<EmployeePassport> getAll() {
        return passportRepository.findAll();
    }

    @Override
    public EmployeePassport update(Long id, EmployeePassport employeePassport) {
        return passportRepository.findById(id).map(
                entity-> {
                    entity.setHandDate(employeePassport.getHandDate());
                    entity.setExpireDate(employeePassport.getExpireDate());
                    entity.setBodyHanded(employeePassport.getBodyHanded());
                    return passportRepository.save(entity);
                }).orElseThrow(()->new NotFoundException("Passport is absent"));
    }

    @Override
    public void remove(Long id) {
        passportRepository.deleteById(id);
    }

    @Override
    public Optional<EmployeePassport> getPassportById(Long id) {
        return passportRepository.findById(id);
    }

    @Override
    public List<EmployeePassport> getAllHanded() {
        return null;
    }

    @Override
    public List<EmployeePassport> getAllNotHanded() {
        return null;
    }

    @Override
    public EmployeePassport addPhoto(Long id, String link) {
        return null;
    }

    @Override
    public EmployeePassport handPassport(Long id, String link) {
        return null;
    }
}
