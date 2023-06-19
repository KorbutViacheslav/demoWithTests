package com.example.demowithtests;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.dto.EmployeeDto;
import com.example.demowithtests.dto.EmployeeReadDto;
import com.example.demowithtests.service.EmployeeSearchService;
import com.example.demowithtests.service.EmployeeService;
import com.example.demowithtests.util.config.mapstruct.EmployeeMapper;
import com.example.demowithtests.web.EmployeeController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Viacheslav Korbut
 * @implNote home task №8.ControllerTests.
 * 1.Import static ArgumentMatchers.
 * 2. Fix MockHttpServletRequestBuilder rebase to mockMvc in createPassTest(), getPassByIdTest(), deletePassTest().
 * 3. Init employee, employeeDto, employeeReadDto for @BeforeEach.
 * 4. Fix getPassByIdTest().
 * 5. Fix deletePassTest().
 * 6. Fix testEntitySave().
 * 7. Created extract methods. Customized employee mapping behavior.
 * 8. Created findByEmailIsNullTest and implemented tests.
 * 9. Created findEmployeesByLowerCaseCountryTest and implemented tests.
 */

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = EmployeeController.class)
@DisplayName("Employee Controller Tests")
public class ControllerTests {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    EmployeeService service;
    @MockBean
    EmployeeSearchService employeeSearchService;
    @MockBean
    EmployeeMapper employeeConverter;
    private Employee employee;
    private EmployeeDto eDto;
    private EmployeeReadDto employeeReadDto;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1).name("Mark").country("uK").email(null).gender(Gender.M).deleted(Boolean.FALSE)
                .build();

        eDto = new EmployeeDto();
        eDto.id = 1;
        eDto.name = "Mark";
        //eDto.email = "test@mail.com";
        eDto.country = "uK";
        eDto.gender = Gender.M;

        employeeReadDto = new EmployeeReadDto();
        employeeReadDto.name = "Mark";
        //employeeReadDto.email = "test@mail.com";
        employeeReadDto.country = "uK";
        employeeReadDto.gender = Gender.M;
    }

    @Test
    @DisplayName("POST /api/users")
    @WithMockUser(roles = "ADMIN")
    public void createPassTest() throws Exception {

        extractedToEmployeeDto();
        extractedToEmployee();
        when(service.create(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));

        verify(service).create(any(Employee.class));
    }


    @Test
    @DisplayName("Entity POST /api/users")
    @WithMockUser(roles = "ADMIN")
    public void testEntitySave() throws Exception {

        extractedToEmployee();
        extractedToReadDto();
        when(this.service.create(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/usersS")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The new employee is successfully created and added to database.")));

        verify(this.service, times(1)).create(any(Employee.class));
        verifyNoMoreInteractions(this.service);
    }

    @Test
    @DisplayName("GET /api/users/{id}")
    @WithMockUser(roles = "USER")
    public void getPassByIdTest() throws Exception {

        extractedToReadDto();
        when(service.getById(1)).thenReturn(employee);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Mark")));

        verify(service).getById(anyInt());
    }

    @Test
    @DisplayName("PUT /api/users/{id}")
    @WithMockUser(roles = "ADMIN")
    public void updatePassByIdTest() throws Exception {

        extractedToEmployee();
        extractedToReadDto();
        when(service.updateById(eq(1), any(Employee.class))).thenReturn(employee);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Employee was successful update!")))
                .andExpect(content().string(containsString("Mark")));

        verify(service).updateById(eq(1), any(Employee.class));
    }


    @Test
    @DisplayName("PATCH /api/users/{id}")
    @WithMockUser(roles = "ADMIN")
    public void deletePassTest() throws Exception {

        doNothing().when(service).removeById(1);

        mockMvc.perform(patch("/api/users/1"))
                .andExpect(status().isOk());

        verify(service).removeById(1);
    }

    @Test
    @DisplayName("GET /api/users/p")
    @WithMockUser(roles = "USER")
    public void getUsersPageTest() throws Exception {

        var employee1 = Employee.builder().id(1).name("John").country("US").build();
        var employee2 = Employee.builder().id(2).name("Jane").country("UK").build();
        var employee3 = Employee.builder().id(3).name("Bob").country("US").build();
        List<Employee> list = asList(employee1, employee2, employee3);
        Page<Employee> employeesPage = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0, 5);

        when(service.getAllWithPagination(eq(pageable))).thenReturn(employeesPage);

        MvcResult result = mockMvc.perform(get("/api/users/p")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andReturn();

        verify(service).getAllWithPagination(eq(pageable));

        String contentType = result.getResponse().getContentType();
        assertNotNull(contentType);
        assertTrue(contentType.contains(MediaType.APPLICATION_JSON_VALUE));
        String responseContent = result.getResponse().getContentAsString();
        assertNotNull(responseContent);
    }

    @Test
    @DisplayName("GET /users/emailsN")
    @WithMockUser(roles = "USER")
    void getEmployeeByEmailIsNullTest() throws Exception {

        List<Employee> list = Collections.emptyList();
        List<EmployeeReadDto> readDtos = asList(employeeReadDto);

        doReturn(readDtos).when(employeeConverter).toListEmployeeReadDto(eq(list));
        when(employeeSearchService.getEmployeeByEmailIsNull()).thenReturn(list);

        mockMvc.perform(get("/api/users/emailsN"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("Mark")));

        verify(employeeSearchService).getEmployeeByEmailIsNull();
    }

    @Test
    @DisplayName("GET /users/countryS")
    @WithMockUser(roles = "USER")
    void getEmployeeByLowerCaseCountryTest() throws Exception {
/*        Employee e1 = Employee.builder()
                .id(1).name("Mark").country("uK").gender(Gender.M).deleted(Boolean.FALSE)
                .build();
        Employee e2 = Employee.builder()
                .id(1).name("Mike").country("USA").gender(Gender.M).deleted(Boolean.FALSE)
                .build();
        Employee e3 = Employee.builder()
                .id(1).name("Milosh").country("poland").gender(Gender.M).deleted(Boolean.FALSE)
                .build();
        List<EmployeeReadDto> ler=Collections.emptyList();
        var le=asList(e1,e2,e3);

        ler=employeeConverter.toListEmployeeReadDto(le);*/

        List<Employee> list = Collections.emptyList();
        List<EmployeeReadDto> readDtos = asList(employeeReadDto);
        when(employeeConverter.toListEmployeeReadDto(eq(list))).thenReturn(readDtos);
        when(employeeSearchService.getByLowerCaseCountry()).thenReturn(list);

        mockMvc.perform(get("/api/users/countryS"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(employeeSearchService).getByLowerCaseCountry();
    }

    private void extractedToReadDto() {
        when(employeeConverter.toReadDto(any(Employee.class))).thenReturn(employeeReadDto);
    }

    private void extractedToEmployee() {
        when(employeeConverter.toEmployee(any(EmployeeDto.class))).thenReturn(employee);
    }

    private void extractedToEmployeeDto() {
        when(employeeConverter.toEmployeeDto(any(Employee.class))).thenReturn(eDto);
    }

}