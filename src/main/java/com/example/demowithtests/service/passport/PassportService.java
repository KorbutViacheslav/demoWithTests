package com.example.demowithtests.service.passport;

import com.example.demowithtests.domain.EmployeePassport;
import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.repository.EmployeePassportRepository;
import com.example.demowithtests.service.photo.PhotoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassportService implements EmployeePassportService {

    private final EmployeePassportRepository passportRepository;
    private final PhotoServiceImpl photoServiceImpl;

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
        EmployeePassport employeePassport = passportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Passport is absent"));
        if (employeePassport.getIsHanded()) {
            throw new EntityNotFoundException("Passport is handed another employee");
        }
        employeePassport.setExpireDate(LocalDateTime.now().plusYears(10));
        employeePassport.setHandDate(new Date());
        employeePassport.setIsHanded(Boolean.TRUE);
        return passportRepository.save(employeePassport);
    }

    @Override
    public void remove(Long id) {
        passportRepository.deleteById(id);
    }

    @Override
    public EmployeePassport getPassportById(Long id) {
        return passportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Passport is absent in database"));
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
    public EmployeePassport pastePhoto(Long passportId, Long photoId) {
        EmployeePassport passport = getPassportById(passportId);
        if (passport.getPhoto() != null) {
            throw new RuntimeException("This passport already has a photo!");
        }
        Photo photo = photoServiceImpl.getPhotoById(photoId);
        passport.setPhoto(photo);
        return passportRepository.save(passport);
    }
}
