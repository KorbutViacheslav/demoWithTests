package com.example.demowithtests.web;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.dto.EmployeeDto;
import com.example.demowithtests.dto.EmployeeReadDto;
import com.example.demowithtests.service.EmployeeCRUDService;
import com.example.demowithtests.service.EmployeeSearchService;
import com.example.demowithtests.service.EmployeeService;
import com.example.demowithtests.util.config.mapstruct.EmployeeMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Employee", description = "Employee API")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeSearchService employeeSearchService;
    private final EmployeeMapper employeeMapper;
    //private final EmployeeCRUDService employeeCRUDService;

    //Save users to database(dto)
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "This is endpoint to add a new employee.", description = "Create request to add a new employee.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED. The new employee is successfully created and added to database."),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found."),
            @ApiResponse(responseCode = "409", description = "Employee already exists")})
    public EmployeeDto saveEmployee(@RequestBody @Valid EmployeeDto requestForSave) {
        var employee = employeeMapper.toEmployee(requestForSave);
        return employeeMapper.toEmployeeDto(employeeService.create(employee));
    }

    //Save users to database
    @PostMapping("/usersS")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> saveEmployee1(@RequestBody EmployeeDto employeeDto) {
        Employee e=employeeMapper.toEmployee(employeeDto);
        EmployeeReadDto readDto = employeeMapper.toReadDto(employeeService.create(e));
        String massage = "The new employee is successfully created and added to database.\n" + readDto.toString();
        return ResponseEntity.ok(massage);
    }

    //Get list all users
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getAllUsers() {
        return employeeMapper.toListEmployeeReadDto(employeeService.getAll());
    }

    @GetMapping("/users/p")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadDto> getPage(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "2") int size) {
        Pageable paging = PageRequest.of(page, size);
        return employeeService.getAllWithPagination(paging).map(employeeMapper::toReadDto);
    }

    //Get user by id
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is endpoint returned a employee by his id.", description = "Create request to read a employee by id", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful. pam pam param."),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found."),
            @ApiResponse(responseCode = "409", description = "Employee already exists")})
    public EmployeeReadDto getEmployeeById(@PathVariable Integer id) {
        log.debug("getEmployeeById() EmployeeController - start: id = {}", id);
        var employee = employeeService.getById(id);
        log.debug("getById() EmployeeController - to dto start: id = {}", id);
        var dto = employeeMapper.toReadDto(employee);
        log.debug("getEmployeeById() EmployeeController - end: name = {}", dto.name);
        return dto;
    }

    //Update user
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> refreshEmployee(@PathVariable("id") Integer id, @RequestBody @Valid EmployeeDto employeeDto) {
        Employee employee = employeeService.updateById(id, employeeMapper.toEmployee(employeeDto));
        EmployeeReadDto readDto = employeeMapper.toReadDto(employee);
        String massage = "Employee was successful update!\n";
        return ResponseEntity.ok(massage + readDto.toString());
    }

    //Remove by id
    @PatchMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> removeEmployeeById(@PathVariable Integer id) {
        employeeService.removeById(id);
        String message = "Employee with ID " + id + " has been deleted.";
        return ResponseEntity.ok(message);
    }

    //Remove all users
    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAllUsers() {
        employeeService.removeAll();
    }

    @GetMapping("/users/country")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadDto> findByCountry(@RequestParam(required = false) String country,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "3") int size,
                                               @RequestParam(defaultValue = "") List<String> sortList,
                                               @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return employeeSearchService.findByCountryContaining(country, page, size, sortList, sortOrder.toString())
                .map(employeeMapper::toReadDto);
    }

    @GetMapping("/users/c")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllUsersC() {
        return employeeSearchService.getAllEmployeeCountry();
    }

    @GetMapping("/users/s")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllUsersSort() {
        return employeeSearchService.getSortCountry();
    }

    @GetMapping("/users/emails")
    @ResponseStatus(HttpStatus.OK)
    public Optional<String> getAllUsersSo() {
        return employeeSearchService.findEmails();
    }

    @GetMapping("/users/countryBy")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getByCountry(@RequestParam(required = true) String country) {
        return employeeMapper.toListEmployeeReadDto(employeeSearchService.filterByCountry(country));
    }

/*    @DeleteMapping("/usersD")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteAllAdmin() {
        employeeCRUDService.removeAllAdmin();
        return ResponseEntity.ok("DELETED ALL USERS!\n" + "ERROR!!!");
    }*/

    /**
     * @implNote
     * home task №6. Get employee by email is null
     * home task №7. Remake the return method of the EmployeeReadDto list
     */
    @GetMapping("/users/emailsN")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getEmployeeByEmailIsNull() {
        return employeeMapper.toListEmployeeReadDto(employeeSearchService.getEmployeeByEmailIsNull());
    }

    /**
     * @implNote
     * home task №6. Get employee by lower case country
     * home task №7. Remake the return method of the EmployeeReadDto list
     */
    @GetMapping("/users/countryS/")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getEmployeeByLowerCaseCountry() {
        return employeeMapper.toListEmployeeReadDto(employeeSearchService.getByLowerCaseCountry());
    }
}
