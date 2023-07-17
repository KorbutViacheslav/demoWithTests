package com.example.demowithtests.service.passport;

import com.example.demowithtests.domain.EmployeePassport;
import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.repository.EmployeePassportRepository;
import com.example.demowithtests.service.photo.PhotoService;
import com.example.demowithtests.util.exception.passport.PassportAlreadyPhotoException;
import com.example.demowithtests.util.exception.passport.PassportIsHandedException;
import com.example.demowithtests.util.exception.passport.PassportNoOneFindException;
import com.example.demowithtests.util.exception.passport.PassportNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassportService implements EmployeePassportService {

    private final EmployeePassportRepository passportRepository;
    private final PhotoService photoService;

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
        EmployeePassport employeePassport = passportRepository
                .findById(id)
                .orElseThrow(PassportNotFoundException::new);
        if (employeePassport.getIsHanded()) {
            throw new PassportIsHandedException();
        }
        employeePassport.setExpireDate(LocalDateTime.now().plusYears(10));
        employeePassport.setHandDate(new Date());
        employeePassport.setIsHanded(Boolean.TRUE);
        return passportRepository.save(employeePassport);
    }

    @Override
    public void remove(Long id) {
        EmployeePassport passport = passportRepository.findById(id)
                .orElseThrow(PassportNotFoundException::new);
        passportRepository.deleteById(passport.getId());
    }

    @Override
    public EmployeePassport getPassportById(Long id) {
        return passportRepository.findById(id)
                .orElseThrow(PassportNotFoundException::new);
    }

    @Override
    public List<EmployeePassport> getAllHanded() {
        List<EmployeePassport> handedPassports = passportRepository.findEmployeePassportByIsHandedTrue();
        if (handedPassports.isEmpty()) {
            throw new PassportNoOneFindException();
        }
        return handedPassports;
    }

    @Override
    public List<EmployeePassport> getAllNotHanded() {
        List<EmployeePassport> handedPassports = passportRepository.findEmployeePassportByIsHandedFalse();
        if (handedPassports.isEmpty()) {
            throw new PassportNoOneFindException();
        }
        return handedPassports;
    }


    @Override
    public EmployeePassport pastePhoto(Long passportId, Long photoId) {
        EmployeePassport passport = getPassportById(passportId);
        if (passport.getPhoto() != null) {
            throw new PassportAlreadyPhotoException();
        }
        Photo photo = photoService.getPhotoById(photoId);
        passport.setPhoto(photo);
        return passportRepository.save(passport);
    }
}
