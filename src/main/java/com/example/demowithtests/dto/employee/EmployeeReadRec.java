package com.example.demowithtests.dto.employee;

import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.domain.WorkPlace;
import com.example.demowithtests.dto.addres.AddressRec;
import com.example.demowithtests.dto.passport.PassportReadRec;
import com.example.demowithtests.dto.passport.PassportRec;

import java.util.Set;

public record EmployeeReadRec(String name,
                              String country,
                              String email,
                              Gender gender,
                              Set<AddressRec> addresses,
                              PassportReadRec passportReadRec,
                              Set<WorkPlace> workPlaces) {
}
