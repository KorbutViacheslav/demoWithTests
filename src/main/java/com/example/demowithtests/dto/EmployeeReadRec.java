package com.example.demowithtests.dto;

import com.example.demowithtests.domain.Gender;

import java.util.Set;

public record EmployeeReadRec(String name,
                              String country,
                              String email,
                              Gender gender,
                              Set<AddressRec> addresses) {
}
