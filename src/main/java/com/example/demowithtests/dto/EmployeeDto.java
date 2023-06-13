package com.example.demowithtests.dto;

import com.example.demowithtests.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class EmployeeDto {

    public Integer id;

    @NotNull(message = "Name may not be null")
    @Size(min = 2, max = 32, message = "Name must be between 2 and 32 characters long")
    @Schema(description = "Name of an employee.", example = "Billy")
    public String name;

    @NotNull
    @Schema(description = "Employee's country name.", example = "England")
    public String country;

    @Email
    @Schema(description = "Employee's email.", example = "billy@gmail.com")
    public String email;

    @NotNull(message = "Gender not be null")
    @Schema(description = "Employee gender.", example = "M")
    public Gender gender;

    public Instant startDate = Instant.now();


    public Set<AddressDto> addresses = new HashSet<>();
}
