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

import javax.validation.Valid;
import java.util.List;

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
    public PassportReadRec getPassportById(@PathVariable Long id) {
        EmployeePassport employeePassport = passportService.getPassportById(id);
        return passportMapper.toPassportReadRec(employeePassport);
    }

    @PutMapping("/users/passport/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassportRec updatePassport(@PathVariable("id") Long id) {
        return passportMapper.toPassportRec(passportService.update(id));
    }

    @DeleteMapping("/users/passport/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> removePassport(@PathVariable("id") Long id) {
        String massage = String.format("Passport with ID %s has been deleted.", id);
        passportService.remove(id);
        return ResponseEntity.ok().body(massage);
    }

    // TODO: 12.07.2023 Example bad controller. Bad practice transfer param to path variable!!!
    @PatchMapping("/user/passport/{passportId}/photo/{photoId}")
    @ResponseStatus(HttpStatus.OK)
    public PassportReadRec pastePhoto(@PathVariable Long passportId,
                                      @PathVariable Long photoId) {
        return passportMapper.toPassportReadRec(passportService.pastePhoto(passportId, photoId));
    }

    @PatchMapping("/user/passport/photo")
    @ResponseStatus(HttpStatus.OK)
    public PassportReadRec pastePhotoHeaders(@RequestHeader("passportId") Long passportId,
                                             @RequestHeader("photoId") Long photoId) {
        return passportMapper.toPassportReadRec(passportService.pastePhoto(passportId, photoId));
    }

}