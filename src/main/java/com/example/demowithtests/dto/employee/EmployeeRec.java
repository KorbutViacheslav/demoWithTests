package com.example.demowithtests.dto.employee;

import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.dto.addres.AddressRec;
import com.example.demowithtests.dto.passport.PassportReadRec;
import com.example.demowithtests.dto.passport.PassportRec;
import com.example.demowithtests.util.annotations.dto.BlockedEmailDomains;
import com.example.demowithtests.util.annotations.dto.CountryRightFormed;
import com.example.demowithtests.util.annotations.dto.NotNullField;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

@NotNullField
public record EmployeeRec(Integer id,
                          @Pattern(regexp = "^[a-z]+$", message = "Invalid username format")
                          @Size(min = 2, max = 32, message = "Name must be between 2 and 32 characters long")
                          @Schema(description = "Name of an employee.", example = "Billy")
                          String name,
                          @CountryRightFormed
                          @Schema(description = "Employee's country name.", example = "EN")
                          String country,
                          @Email
                          @BlockedEmailDomains
                          @Schema(description = "Employee's email.", example = "billy@gmail.com")
                          String email,
                          @Schema(description = "Employee gender.", example = "M")
                          Gender gender,
                          Instant startDate,
                          Set<AddressRec> addresses,
                          PassportReadRec passportReadRec) {
    public EmployeeRec {
        startDate = Instant.now();
    }
}
