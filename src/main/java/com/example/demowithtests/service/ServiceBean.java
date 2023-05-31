package com.example.demowithtests.service;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.repository.Repository;
import com.example.demowithtests.util.ResourceNotFoundException;
import com.example.demowithtests.util.ResourceWasDeletedException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@org.springframework.stereotype.Service
public class ServiceBean implements Service {

    private final Repository repository;

    @Override
    public Employee create(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employeeList = repository.findAll();
        employeeList.removeIf(Employee::isDeleted);
        if (employeeList.isEmpty()) {
            throw new EntityNotFoundException("Employees not found!");
        }
        return employeeList;
    }

    @Override
    public Employee getById(Integer id) {
        return repository.findById(id).filter(e -> !e.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));
    }

    @Override
    public Employee updateById(Integer id, Employee employee) {
        return repository.findById(id)
                .map(entity -> {
                    entity.setName(employee.getName());
                    entity.setEmail(employee.getEmail());
                    entity.setCountry(employee.getCountry());
                    /**!!!!!!*/
                    entity.setDeleted(employee.isDeleted());
                    return repository.save(entity);
                })
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));
    }

    @Override
    public void removeById(Integer id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));
        employee.setDeleted(true);
        repository.save(employee);
    }

    /**
     * @Autor Viacheslav Korbut
     * Removes from the database. Use only by administrators.
     */
    @Override
    public void removeByIdAdmin(Integer id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));
        repository.delete(employee);
    }

    @Override
    public void removeAll() {
        getAll().forEach(employee -> {
            employee.setDeleted(true);
            repository.save(employee);
        });
    }
}
