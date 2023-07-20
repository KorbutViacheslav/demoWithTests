package com.example.demowithtests.web;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.dto.employee.EmployeeReadRec;
import com.example.demowithtests.dto.employee.EmployeeRec;
import com.example.demowithtests.service.employee.EmployeeSearchService;
import com.example.demowithtests.service.employee.EmployeeService;
import com.example.demowithtests.util.config.mapstruct.EmployeeMapper;
import com.example.demowithtests.util.config.swagger.EmployeeControllerApi;
import lombok.AllArgsConstructor;
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
public class EmployeeController implements EmployeeControllerApi {

    private final EmployeeService employeeService;
    private final EmployeeSearchService employeeSearchService;
    private final EmployeeMapper employeeMapper;

    //Save users to database(dto)
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeRec saveEmployee(@RequestBody @Valid EmployeeRec requestForSave) {
        var employee = employeeMapper.toEmployee(requestForSave);
        return employeeMapper.toEmployeeDto(employeeService.create(employee));
    }

    @PostMapping("/usersS")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> saveEmployee1(@RequestBody EmployeeRec request) {
        Employee e = employeeMapper.toEmployee(request);
        EmployeeReadRec readDto = employeeMapper.toReadRec(employeeService.create(e));
        String massage = "The new employee is successfully created and added to database.\n" + readDto.toString();
        return ResponseEntity.ok(massage);
    }


    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadRec> getAllUsers() {
        return employeeMapper.toListEmployeeReadDto(employeeService.getAll());
    }

    @GetMapping("/users/p")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadRec> getPage(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "2") int size) {
        Pageable paging = PageRequest.of(page, size);
        return employeeService.getAllWithPagination(paging).map(employeeMapper::toReadRec);
    }

    //Get user by id
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReadRec getEmployeeById(@PathVariable Integer id) {
        var employee = employeeService.getById(id);
        var dto = employeeMapper.toReadRec(employee);
        return dto;
    }

    //Update user
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> refreshEmployee(@PathVariable("id") Integer id, @RequestBody @Valid EmployeeRec request) {
        Employee employee = employeeService.updateById(id, employeeMapper.toEmployee(request));
        EmployeeReadRec readDto = employeeMapper.toReadRec(employee);
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
    public Page<EmployeeReadRec> findByCountry(@RequestParam(required = false) String country,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "3") int size,
                                               @RequestParam(defaultValue = "") List<String> sortList,
                                               @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return employeeSearchService.findByCountryContaining(country, page, size, sortList, sortOrder.toString())
                .map(employeeMapper::toReadRec);
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
    public List<EmployeeReadRec> getByCountry(@RequestParam(required = true) String country) {
        return employeeMapper.toListEmployeeReadDto(employeeSearchService.filterByCountry(country));
    }

    /**
     * @implNote home task №6. Get employee by email is null
     * home task №7. Remake the return method of the EmployeeReadDto list
     */
    @GetMapping("/users/emailsN")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadRec> getEmployeeByEmailIsNull() {
        return employeeMapper.toListEmployeeReadDto(employeeSearchService.getEmployeeByEmailIsNull());
    }

    /**
     * @implNote home task №6. Get employee by lower case country
     * home task №7. Remake the return method of the EmployeeReadDto list
     */
    @GetMapping("/users/countryS")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadRec> getEmployeeByLowerCaseCountry() {
        return employeeMapper.toListEmployeeReadDto(employeeSearchService.getByLowerCaseCountry());
    }

    /**
     * @implNote home task №9. Get all Ukrainian from database.
     */
    @GetMapping("/users/ua")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadRec> getAllUkrainian() {
        return employeeMapper.toListEmployeeReadDto(employeeSearchService.getAllUkrainian());
    }

    /**
     * @implNote home task №9. Get all home losses.
     */
    @GetMapping("/users/addresses")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadRec> getEmployeeNullAddresses() {
        return employeeMapper.toListEmployeeReadDto(employeeSearchService.getEmployeeNullAddresses());
    }

    /**
     * @implNote home task №13. Handed passport to employee.
     * Used request header to transfer param.
     */

    @PatchMapping("/user/passport/")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReadRec handedPassport(@RequestHeader("employeeId") Integer employeeId,
                                          @RequestHeader("passportId") Long passportId) {
        return employeeMapper.toReadRec(employeeService.handPassport(employeeId, passportId));
    }

    /**
     * @implNote home task №14. Reserve work place.
     */
    @PatchMapping("/user/place")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReadRec reserveWorkPlace(@RequestHeader("employeeId") Integer employeeId,
                                            @RequestHeader("workPlaceId") Long workPlaceId) {
        return employeeMapper.toReadRec(employeeService.reserveWorkPlace(employeeId, workPlaceId));
    }
}
