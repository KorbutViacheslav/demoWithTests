package com.example.demowithtests.service.employee;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.EmployeePassport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmployeeService {

    Employee create(Employee employee);

    Employee createEM(Employee employee);
    Employee getEmployeeByIdEM(Integer id);
    List<Employee> getAllEM();

    List<Employee> getAll();

    Page<Employee> getAllWithPagination(Pageable pageable);

    Employee getById(Integer id);

    Employee updateById(Integer id, Employee plane);

    void removeById(Integer id);

    void removeAll();

    Employee handPassport(Integer employeeId, Long passportId);

    Employee reserveWorkPlace(Integer employeeId, Long workPlaceId);

    void deprivePassport(Integer id);

    Map<Long,Employee> getEmployeesWithDeprivePassports();
    Map<String,String> getHistoryPassport();
    Map<String,List<String>> getEmployeePassportHistory(Integer employeeId);

}
