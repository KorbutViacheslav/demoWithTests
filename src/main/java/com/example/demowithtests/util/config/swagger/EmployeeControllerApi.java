package com.example.demowithtests.util.config.swagger;

import com.example.demowithtests.dto.employee.EmployeeReadRec;
import com.example.demowithtests.dto.employee.EmployeeRec;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Viacheslav Korbut
 * home task №11. Created interface from EmployeeController.
 */
@Tag(name = "Employee", description = "Employee API")
public interface EmployeeControllerApi {

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "This is endpoint to add a new employee.",
            description = "Create request to add a new employee.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED. The new employee is successfully created and added to database."),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "409", description = "Employee already exists")})
    EmployeeRec saveEmployee(@RequestBody @Valid EmployeeRec requestForSave);


    @PostMapping("/usersS")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "This is endpoint to add a new employee.",
            description = "Create request to add a new employee.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED. The new employee is successfully created and added to database."),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "409", description = "Employee already exists")})
    ResponseEntity<String> saveEmployee1(@RequestBody EmployeeRec request);


    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is endpoint to get all employee.",
            description = "Create request to get all employee.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Employees not found.")})
    List<EmployeeReadRec> getAllUsers();

    @GetMapping("/users/p")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is endpoint to get all employee and paginates.",
            description = "Create request to get all employee and paginates.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful."),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Employees not found.")})
    Page<EmployeeReadRec> getPage(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "2") int size);

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is endpoint returned a employee by his id.",
            description = "Create request to read a employee by id", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok."),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found."),
            @ApiResponse(responseCode = "409", description = "Employee already exists")})
    EmployeeReadRec getEmployeeById(@PathVariable Integer id);

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is endpoint returned refresh employee by his id.",
            description = "Create request to refresh employee by id", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful."),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found."),
            @ApiResponse(responseCode = "409", description = "Employee already exists")})
    ResponseEntity<String> refreshEmployee(@PathVariable("id") Integer id, @RequestBody @Valid EmployeeRec request);

    @PatchMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "This is endpoint to remove employee by his id.",
            description = "Create request to remove employee by his id.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful."),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Employees not found.")})
    ResponseEntity<String> removeEmployeeById(@PathVariable Integer id);


    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "This is endpoint to remove all employee.",
            description = "Create request to remove all employee.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Employees not found.")})
    void removeAllUsers();

    @GetMapping("/users/country")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is endpoint returned a employee by his country.",
            description = "Create request to read a employee by his country", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok."),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found.")})
    Page<EmployeeReadRec> findByCountry(@RequestParam(required = false) String country,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "3") int size,
                                        @RequestParam(defaultValue = "") List<String> sortList,
                                        @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder);

    @GetMapping("/users/c")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is endpoint returned a employees country.",
            description = "Create request to read a employees country", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found.")})
    List<String> getAllUsersC();

    @GetMapping("/users/s")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is the endpoint returning sorted employee countries by 'U' char.",
            description = "Create request to read sorted employee countries by 'U' char", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found.")})
    List<String> getAllUsersSort();

    @GetMapping("/users/emails")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is the endpoint returning employees emails.",
            description = "Create request to read employees emails.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found.")})
    Optional<String> getAllUsersSo();

    @GetMapping("/users/countryBy")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is the endpoint returning employees by his country.",
            description = "Create request to read employees by his country.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found.")})
    List<EmployeeReadRec> getByCountry(@RequestParam(required = true) String country);

    @GetMapping("/users/emailsN")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is the endpoint returning employees where email is null.",
            description = "Create request to read employees where email is null.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found.")})
    List<EmployeeReadRec> getEmployeeByEmailIsNull();


    @GetMapping("/users/countryS")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is the endpoint returning employees by lower case country.",
            description = "Create request to read employees by lower case country.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found.")})
    List<EmployeeReadRec> getEmployeeByLowerCaseCountry();

    @GetMapping("/users/ua")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is the endpoint returning all Ukrainian employees.",
            description = "Create request to read Ukrainian employees.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found.")})
    List<EmployeeReadRec> getAllUkrainian();


    @GetMapping("/users/addresses")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "This is the endpoint returning employees where addresses is absent.",
            description = "Create request to read employees where addresses is absent.", tags = {"Employee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND. Specified employee request not found.")})
    List<EmployeeReadRec> getEmployeeNullAddresses();
}
