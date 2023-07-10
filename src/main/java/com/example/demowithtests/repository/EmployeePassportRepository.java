package com.example.demowithtests.repository;

import com.example.demowithtests.domain.EmployeePassport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeePassportRepository extends JpaRepository<EmployeePassport,Long> {
/*    List<EmployeePassport> getAllHanded();
    List<EmployeePassport> getAllNotHanded();*/
}
