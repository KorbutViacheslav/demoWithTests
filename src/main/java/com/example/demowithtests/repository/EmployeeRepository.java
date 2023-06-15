package com.example.demowithtests.repository;

import com.example.demowithtests.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    //@Query(value = "SELECT * FROM users", nativeQuery = true)

    @Query(value = "select e from Employee e where e.country =?1")
    List<Employee> findByCountry(String country);

    @Query(value = "select * from users join addresses on users.id = addresses.employee_id " +
            "where users.gender = :gender and addresses.country = :country", nativeQuery = true)
    List<Employee> findByGender(String gender, String country);

    Employee findByName(String name);

    Employee findEmployeeByEmailNotNull();

    @NotNull
    Page<Employee> findAll(Pageable pageable);

    Page<Employee> findByName(String name, Pageable pageable);

    Page<Employee> findByCountryContaining(String country, Pageable pageable);

    /**
     * @implNote
     * home task №6. Get employee by email if is null
     */
    @Query(value = "SELECT * FROM users WHERE email IS NULL AND deleted = false", nativeQuery = true)
    List<Employee> findByEmailIsNull();

    /**
     * @implNote
     * home task №6. Get employee by lower case country
     */
    @Query(value = "SELECT * FROM users WHERE (ASCII(SUBSTRING(country, 1, 1)) BETWEEN 97 AND 122) AND deleted = false",
            nativeQuery = true)
    List<Employee> findEmployeesByLowerCaseCountry();

}
