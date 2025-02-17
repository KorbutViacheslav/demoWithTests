package com.example.demowithtests.service.employee;

import com.example.demowithtests.domain.Employee;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface EmployeeSearchService {
    //Page<Employee> findByCountryContaining(String country, Pageable pageable);

    /**
     * @param country   Filter for the country if required
     * @param page      number of the page returned
     * @param size      number of entries in each page
     * @param sortList  list of columns to sort on
     * @param sortOrder sort order. Can be ASC or DESC
     * @return Page object with customers after filtering and sorting
     */

    Page<Employee> findByCountryContaining(String country, int page, int size, List<String> sortList, String sortOrder);

    /**
     * Get all the countries of all the employees.
     *
     * @return A list of all the countries that employees are from.
     */
    List<String> getAllEmployeeCountry();

    /**
     * It returns a list of countries sorted by name.
     *
     * @return A list of countries in alphabetical order.
     */
    List<String> getSortCountry();

    Optional<String> findEmails();

    List<Employee> filterByCountry(String country);

    /**
     * @author Viacheslav Korbut
     * home task №6. Get employee by email is null
     */
    List<Employee> getEmployeeByEmailIsNull();

    /**
     * @author Viacheslav Korbut
     * home task №6. Get employee by lower case country
     */
    List<Employee> getByLowerCaseCountry();
    /**
     * home task №9. Get all Ukrainian from database.
     */
    List<Employee> getAllUkrainian();
    /**
     * home task №9. Get all home losses.
     */
    List<Employee> getEmployeeNullAddresses();
}
