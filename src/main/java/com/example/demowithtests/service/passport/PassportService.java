package com.example.demowithtests.service.passport;

import com.example.demowithtests.domain.EmployeePassport;
import com.example.demowithtests.repository.EmployeePassportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.Date;
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
    public EmployeePassport update(Long id) {
        LocalDateTime localDateTime = LocalDateTime.now().plusYears(10);
        return passportRepository.findById(id).map(
                entity-> {
                    entity.setHandDate(new Date());
                    entity.setExpireDate(localDateTime);
                    entity.setIsHanded(Boolean.TRUE);
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
        return passportRepository.findEmployeePassportByIsHandedTrue();
    }

    @Override
    public List<EmployeePassport> getAllNotHanded() {
        return passportRepository.findEmployeePassportByIsHandedFalse();
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
