package com.example.demowithtests;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.repository.EmployeeRepository;
import com.example.demowithtests.service.employee.EmployeeCRUDService;
import com.example.demowithtests.service.employee.EmployeeSearchSortService;
import com.example.demowithtests.util.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @author Viacheslav Korbut
 * @implNote home task №8.ServiceTests.
 * 1. Import static ArgumentMatchers.
 * 2. Fix deleteEmployeeTest().
 * 3. Inject EmployeeSearchSortService.
 * 4. Created findByEmailIsNullTest and write 5 tests. And getEmployeeByEmailIsNullThrowTest.
 * 5. Created findEmployeesByLowerCaseCountryTest and write 5 tests. And getByLowerCaseCountryThrowTest.
 * 6. Used assertAll.
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("Employee Service Tests")
public class ServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeCRUDService service;

    @InjectMocks
    private EmployeeSearchSortService searchService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder().id(1).name("Mark").country("hungary").gender(Gender.M).deleted(Boolean.FALSE)
                //.email("test@mail.com")
                .build();
    }

    @Test
    @DisplayName("Save employee test")
    public void whenSaveEmployee_shouldReturnEmployee() {

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        var created = service.create(employee);

        assertThat(created.getName()).isSameAs(employee.getName());

        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("Get employee by exist id test")
    public void whenGivenId_shouldReturnEmployee_ifFound() {

        Employee employee = new Employee();
        employee.setId(88);

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        assertThat(service.getById(employee.getId())).isSameAs(employee);

        verify(employeeRepository).findById(employee.getId());
    }

    @Test
    @DisplayName("Throw exception when employee not found test")
    public void should_throw_exception_when_employee_doesnt_exist() {

        when(employeeRepository.findById(anyInt())).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> employeeRepository.findById(anyInt()));
    }

    @Test
    @DisplayName("Read employee by id test")
    public void readEmployeeByIdTest() {

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        assertThat(service.getById(employee.getId())).isSameAs(employee);

        verify(employeeRepository).findById(employee.getId());
    }

    @Test
    @DisplayName("Read all employees test")
    public void readAllEmployeesTest() {

        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        var list = employeeRepository.findAll();

        assertThat(list.size()).isGreaterThan(0);

        verify(employeeRepository).findAll();
    }

    @Test
    @DisplayName("Delete employee test")
    public void deleteEmployeeTest() {

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        assertEquals(employee.isDeleted(), Boolean.FALSE);

        service.removeById(employee.getId());

        assertEquals(employee.isDeleted(), Boolean.TRUE);

        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("Get employee by email if is null")
    void getEmployeeByEmailIsNullTest() {

        when(employeeRepository.findByEmailIsNull()).thenReturn(List.of(employee));

        var list = employeeRepository.findByEmailIsNull();
        assertAll(
                () -> assertThat(list.size()).isGreaterThan(0),
                () -> assertThat(list.get(0).getEmail()).isNull(),
                () -> assertThat(list.get(0).getName()).isEqualTo("Mark")
        );
        // Перевірка, що findByEmailIsNull() було викликано лише один раз
        verify(employeeRepository, times(1)).findByEmailIsNull();
        // Перевірка, що інші методи не були викликані
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    @DisplayName("Get employee by email if is null - EntityNotFoundException")
    void getEmployeeByEmailIsNullThrowTest() {

        when(employeeRepository.findByEmailIsNull()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> searchService.getEmployeeByEmailIsNull());
    }

    @Test
    @DisplayName("Get employee by lower case country")
    void getByLowerCaseCountryTest() {

        when(employeeRepository.findEmployeesByLowerCaseCountry()).thenReturn(List.of(employee));

        var list = employeeRepository.findEmployeesByLowerCaseCountry();
        assertAll(
                () -> assertThat(list.size()).isGreaterThan(0),
                () -> assertThat(list.get(0).getName()).isEqualTo("Mark"),
                () -> list.forEach(employee -> {
                    assertThat(employee.getId()).isNotNull();
                    assertThat(employee.getCountry()).isEqualTo(employee.getCountry().toLowerCase());
                })
        );
        verify(employeeRepository, times(1)).findEmployeesByLowerCaseCountry();
    }

    @Test
    @DisplayName("Get employee by lower case country - EntityNotFoundException")
    void getByLowerCaseCountryThrowTest() {

        when(employeeRepository.findEmployeesByLowerCaseCountry()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> searchService.getByLowerCaseCountry());
    }

}
