package com.example.demowithtests.service;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.repository.EmployeeRepository;
import com.example.demowithtests.util.exception.EmployeeContainsException;
import com.example.demowithtests.util.exception.ResourceNotFoundException;
import com.example.demowithtests.util.exception.ResourceWasDeletedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeCRUDService implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    // @Transactional(propagation = Propagation.MANDATORY)
    public Employee create(Employee employee) {
        comparisonEmployee(employee);
        return employeeRepository.save(employee);
    }


    @Override
    public Employee createEM(Employee employee) {
        return entityManager.merge(employee);
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employeeList = employeeRepository.findAll()
                .stream()
                .filter(employee -> !employee.isDeleted())
                .sorted(Comparator.comparing(Employee::getId))
                .collect(Collectors.toList());
        if (employeeList.isEmpty()) {
            return Collections.emptyList();
        }
        return employeeList;
    }

    @Override
    public Page<Employee> getAllWithPagination(Pageable pageable) {
        Page<Employee> list = employeeRepository.findAll(pageable);
        return list;
    }

    @Override
    public Employee getById(Integer id) {
        return employeeRepository.findById(id)
                .filter(e -> !e.isDeleted())
                .orElseThrow(() -> (employeeRepository.existsById(id))
                        ? new ResourceWasDeletedException()
                        : new ResourceNotFoundException()
                );
    }

    @Override
    public Employee updateById(Integer id, Employee employee) {
        comparisonEmployee(employee);
        return employeeRepository.findById(id)
                .map(entity -> {
                    entity.setName(employee.getName());
                    entity.setEmail(employee.getEmail());
                    entity.setCountry(employee.getCountry());
                    entity.setGender(employee.getGender());
                    return employeeRepository.save(entity);
                })
                .orElseThrow(() -> (employeeRepository.existsById(id))
                        ? new ResourceWasDeletedException()
                        : new ResourceNotFoundException()
                );
    }

    @Override
    public void removeById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> (employeeRepository.existsById(id))
                        ? new ResourceWasDeletedException()
                        : new ResourceNotFoundException()
                );
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }


    @Override
    public void removeAll() {
        getAll().forEach(employee -> {
            employee.setDeleted(true);
            employeeRepository.save(employee);
        });
    }

    public void removeAllAdmin() {
        employeeRepository.deleteAll();
    }

    private boolean comparisonEmployee(Employee employee) {
        getAll().stream().filter(emp ->
                        Objects.equals(emp.getName(), employee.getName()) &&
                                Objects.equals(emp.getCountry(), employee.getCountry()) &&
                                Objects.equals(emp.getEmail(), employee.getEmail()) &&
                                Objects.equals(emp.getGender(), employee.getGender()))
                .findFirst()
                .ifPresent(emp -> {
                    throw new EmployeeContainsException();
                });
        return true;
    }
   /* public boolean isValid(Employee employee) {
        String regex = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(employee.getPhone());
        boolean isFound = matcher.find();
        if (isFound) {
            System.out.println("Number is valid");
            return true;
        } else {
            System.out.println("Number is invalid");
            return false;
        }
    }*/

    /*public boolean isVodafone(Employee employee) {
        String regex = "^[0][9][5]{1}[0-9]{7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(employee.getPhone());
        boolean isFound = matcher.find();
        if (isFound) {
            System.out.println("Number is Vodafone");
            return true;
        } else {
            System.out.println("Number is not Vodafone");
            return false;
        }
    }*/

}
