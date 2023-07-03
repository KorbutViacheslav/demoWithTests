package com.example.demowithtests;

import com.example.demowithtests.domain.Address;
import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * @author Viacheslav Korbut
 * @implNote home task №8. RepositoryTests.
 * 1. Import static Assertions.
 * 2. Created another employees in first order with null email and first lower case country.
 * 3. Used assert all().
 * 4. Created findByEmailIsNullTest and write 3 tests.
 * 5. Created findEmployeesByLowerCaseCountryTest and write 5 tests.
 */

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Employee Repository Tests")
public class RepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("Save employee test")
    public void saveEmployeeTest() {

        var employee = Employee.builder().name("Mark").country("England")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("UK")
                                .build())))
                .gender(Gender.M)
                .build();
        var e = Employee.builder().name("Adam").country("Israel").gender(Gender.M).deleted(Boolean.FALSE)
                .build();
        var eCountry = Employee.builder().name("Marselo").country("mexico").gender(Gender.M).deleted(Boolean.FALSE)
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(e);
        employeeRepository.save(eCountry);

        assertThat(employee.getId()).isGreaterThan(0);
        assertThat(employee.getId()).isEqualTo(1);
        assertThat(employee.getName()).isEqualTo("Mark");
    }

    @Test
    @Order(2)
    @DisplayName("Get employee by id test")
    public void getEmployeeTest() {

        var employee = employeeRepository.findById(1).orElseThrow();

        assertThat(employee.getId()).isEqualTo(1);
        assertThat(employee.getName()).isEqualTo("Mark");
    }

    @Test
    @Order(3)
    @DisplayName("Get employees test")
    public void getListOfEmployeeTest() {

        var employeesList = employeeRepository.findAll();

        assertThat(employeesList.size()).isGreaterThan(0);

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("Update employee test")
    public void updateEmployeeTest() {

        var employee = employeeRepository.findById(1).orElseThrow();

        employee.setName("Martin");
        var employeeUpdated = employeeRepository.save(employee);

        assertThat(employeeUpdated.getName()).isEqualTo("Martin");

    }

    @Test
    @Order(5)
    @DisplayName("Find employee by gender test")
    public void findByGenderTest() {

        var employees = employeeRepository.findByGender(Gender.M.toString(), "UK");

        assertThat(employees.get(0).getGender()).isEqualTo(Gender.M);
    }

    @Test
    @Order(6)
    @Rollback(value = false)
    @DisplayName("Delete employee test")
    public void deleteEmployeeTest() {

        var employee = employeeRepository.findById(1).orElseThrow();

        employeeRepository.delete(employee);

        Employee employeeNull = null;

        var optionalEmployee = Optional.ofNullable(employeeRepository.findByName("Martin"));

        if (optionalEmployee.isPresent()) {
            employeeNull = optionalEmployee.orElseThrow();
        }

        assertThat(employeeNull).isNull();
    }

    @Test
    @Order(7)
    @DisplayName("Get employee by email if is null")
    void findByEmailIsNullTest() {

        var employees = employeeRepository.findByEmailIsNull();

        assertAll(
                () -> assertThat(employees.size()).isGreaterThan(0),
                () -> assertThat(employees.get(0).getName()).isEqualTo("Adam"),
                () -> assertThat(employees.get(0).getId()).isEqualTo(2));
    }

    @Test
    @Order(8)
    @DisplayName("Get employee by lower case country")
    void findEmployeesByLowerCaseCountryTest() {

        var employees = employeeRepository.findEmployeesByLowerCaseCountry();

        assertAll(
                () -> assertThat(employees.size()).isGreaterThan(0),
                () -> assertThat(employees.get(0).getName()).isEqualTo("Marselo"),
                () -> assertThat(employees.get(0).getId()).isEqualTo(3),
                () -> employees.forEach(employee -> {
                    assertThat(employee.getCountry()).isNotNull().isNotEmpty();
                    assertThat(employee.getCountry()).isEqualTo(employee.getCountry().toLowerCase());
                })
        );
    }
}
