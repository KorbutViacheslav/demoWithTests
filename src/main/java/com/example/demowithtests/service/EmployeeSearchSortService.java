package com.example.demowithtests.service;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeSearchSortService implements EmployeeSearchService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeCRUDService employeeCRUDService;

        /*@Override
    public Page<Employee> findByCountryContaining(String country, Pageable pageable) {
        return employeeRepository.findByCountryContaining(country, pageable);
    }*/

    @Override
    public List<String> getAllEmployeeCountry() {
        log.info("getAllEmployeeCountry() - start:");

        List<Employee> employeeList = employeeRepository.findAll();
        List<String> countries = employeeList.stream()
                .map(Employee::getCountry)
                .collect(Collectors.toList());

        log.info("getAllEmployeeCountry() - end: countries = {}", countries);
        return countries;
    }

    @Override
    public Page<Employee> findByCountryContaining(String country, int page, int size, List<String> sortList, String sortOrder) {
        // create Pageable object using the page, size and sort details
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        // fetch the page object by additionally passing pageable with the filters
        return employeeRepository.findByCountryContaining(country, pageable);
    }

    @Override
    public List<String> getSortCountry() {
        List<Employee> employeeList = employeeRepository.findAll();
        return employeeList.stream()
                .map(Employee::getCountry)
                .filter(c -> c.startsWith("U"))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<String> findEmails() {
        var employeeList = employeeRepository.findAll();

        var emails = employeeList.stream()
                .map(Employee::getEmail)
                .collect(Collectors.toList());

        var opt = emails.stream()
                .filter(s -> s.endsWith(".com"))
                .findFirst()
                .orElse("error?");
        return Optional.ofNullable(opt);
    }


    @Override
    public List<Employee> filterByCountry(String country) {
        return employeeRepository.findByCountry(country);
    }

    /**
     * @implNote home task №6. Get employee by email is null
     */
    @Override
    public List<Employee> getEmployeeByEmailIsNull() {
        log.debug("getEmployeeByEmailIsNull() EmployeeSearchSortService - start");
        List<Employee> employees = employeeRepository.findByEmailIsNull();
        if (employees.isEmpty()) {
            throw new EntityNotFoundException("Employees not found!");
        }
        log.debug("getEmployeeByEmailIsNull() EmployeeSearchSortService - end");
        return employees;
    }

    /**
     * @implNote home task №6. Get employee by lower case country
     */
    @Override
    public List<Employee> getByLowerCaseCountry() {
        log.debug("getByLowerCaseCountry() EmployeeSearchSortService - start");
        List<Employee> employees = employeeRepository.findEmployeesByLowerCaseCountry()
                .stream().peek(e -> e.setCountry(StringUtils.capitalize(e.getCountry())))
                .collect(Collectors.toList());
        if (employees.isEmpty()) {
            throw new EntityNotFoundException("Employees not found!");
        }
        log.debug("getByLowerCaseCountry() EmployeeSearchSortService - end");
        return employees;
    }
    /**
     * @implNote home task №9. Get all Ukrainian from database.
     */
    @Override
    public List<Employee> getAllUkrainian() {
        return employeeRepository.findAllUkrainian();
    }

    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
    }
}
