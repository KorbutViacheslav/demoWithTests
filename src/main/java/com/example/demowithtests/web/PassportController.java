package com.example.demowithtests.web;

import com.example.demowithtests.domain.EmployeePassport;
import com.example.demowithtests.dto.passport.PassportReadRec;
import com.example.demowithtests.dto.passport.PassportRec;
import com.example.demowithtests.service.passport.EmployeePassportService;
import com.example.demowithtests.util.config.mapstruct.PassportMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Passport", description = "Passport API")
public class PassportController {
    private final EmployeePassportService passportService;
    private final PassportMapper passportMapper;

    @PostMapping("/users/passport")
    @ResponseStatus(HttpStatus.CREATED)
    public PassportRec createdPassport(@RequestBody @Valid PassportRec passportRec) {
        var employeePassport = passportMapper.toEmployeePassport(passportRec);
        return passportMapper.toPassportRec(passportService.create(employeePassport));
    }

    @GetMapping("/users/passports")
    @ResponseStatus(HttpStatus.OK)
    public List<PassportReadRec> getAllPassports() {
        return passportMapper.toListPassportReadRec(passportService.getAll());
    }

    @GetMapping("/users/passport/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassportReadRec getPassport(@PathVariable Long id) {
        Optional<EmployeePassport> employeePassport = passportService.getPassportById(id);
        if (employeePassport.isPresent()) {
            EmployeePassport ep = employeePassport.get();
            return passportMapper.toPassportReadRec(ep);
        } else {
            throw new EntityNotFoundException("Passport is absent in database");
        }
    }

    @PutMapping("/users/passport/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassportRec updatePassport(@PathVariable("id") Long id) {
        return passportMapper.toPassportRec(passportService.update(id));
    }
    @DeleteMapping("/users/passport/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> removePassport(@PathVariable("id") Long id){
        String massage =  "Passport with ID " + id + " has been deleted.";
        passportService.remove(id);
        return ResponseEntity.ok(massage);
    }

}
