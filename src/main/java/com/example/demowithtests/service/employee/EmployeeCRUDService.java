package com.example.demowithtests.service.employee;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.EmployeePassport;
import com.example.demowithtests.domain.Reservation;
import com.example.demowithtests.repository.EmployeePassportRepository;
import com.example.demowithtests.repository.EmployeeRepository;
import com.example.demowithtests.service.passport.EmployeePassportService;
import com.example.demowithtests.service.work_place.ReserveService;
import com.example.demowithtests.util.annotations.entity.ActivateCustomAnnotations;
import com.example.demowithtests.util.annotations.entity.Name;
import com.example.demowithtests.util.annotations.entity.ToLowerCase;
import com.example.demowithtests.util.exception.employee.EmployeeContainsException;
import com.example.demowithtests.util.exception.employee.ResourceNotFoundException;
import com.example.demowithtests.util.exception.employee.ResourceWasDeletedException;
import com.example.demowithtests.util.exception.passport.PassportNoOneFindException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeCRUDService implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeePassportService employeePassportService;
    private final EmployeePassportRepository employeePassportRepository;
    private final ReserveService reserveService;

    @PersistenceContext
    private EntityManager entityManager;

    @ActivateCustomAnnotations({ToLowerCase.class, Name.class})
    @Override
    // @Transactional(propagation = Propagation.MANDATORY)
    public Employee create(Employee employee) {
        comparisonEmployee(employee);
        return employeeRepository.save(employee);
    }


    @Override
    public Employee createEM(Employee employee) {
        return entityManager.merge(employee);
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employeeList = employeeRepository.findAll()
                .stream()
                .filter(employee -> !employee.isDeleted())
                .sorted(Comparator.comparing(Employee::getId))
                .collect(Collectors.toList());
        if (employeeList.isEmpty()) {
            return Collections.emptyList();
        }
        return employeeList;
    }

    @Override
    public Page<Employee> getAllWithPagination(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Employee getById(Integer id) {
        return employeeRepository.findById(id)
                .filter(e -> !e.isDeleted())
                .orElseThrow(() -> (employeeRepository.existsById(id))
                        ? new ResourceWasDeletedException()
                        : new ResourceNotFoundException()
                );
    }

    @Override
    public Employee updateById(Integer id, Employee employee) {
        comparisonEmployee(employee);
        return employeeRepository.findById(id)
                .map(entity -> {
                    entity.setName(employee.getName());
                    entity.setEmail(employee.getEmail());
                    entity.setCountry(employee.getCountry());
                    entity.setGender(employee.getGender());
                    return employeeRepository.save(entity);
                })
                .orElseThrow(() -> (employeeRepository.existsById(id))
                        ? new ResourceWasDeletedException()
                        : new ResourceNotFoundException()
                );
    }

    @Override
    public void removeById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> (employeeRepository.existsById(id))
                        ? new ResourceWasDeletedException()
                        : new ResourceNotFoundException()
                );
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }


    @Override
    public void removeAll() {
        getAll().forEach(employee -> {
            employee.setDeleted(true);
            employeeRepository.save(employee);
        });
    }

    @Override
    public Employee handPassport(Integer employeeId, Long passportId) {
        Employee employee = getById(employeeId);
        if (employee.getEmployeePassport() != null) {
            if (employee.getEmployeePassport().getIsHanded()) {
                throw new RuntimeException("This employee have passport");
            }
        }
        EmployeePassport employeePassport = employeePassportService
                .update(passportId);
        employee.setEmployeePassport(employeePassport);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee reserveWorkPlace(Integer employeeId, Long workPlaceId) {
        var employee = getById(employeeId);
        var reservation = reserveService.create(employeeId, workPlaceId);
        Set<Reservation> reservations = new HashSet<>();
        reservations.add(reservation);
        employee.setReservations(reservations);
        return employeeRepository.save(employee);
    }

    /**
     * @apiNote Homework_15. Deprive passport employee.
     */
    @Override
    public void deprivePassport(Integer id) {
        var employee = getById(id);
        var passport = employee.getEmployeePassport();
        if (passport != null) {
            passport.setPreviousPassportId(employee.getId());
            passport.setIsHanded(Boolean.FALSE);
            employeePassportRepository.save(passport);
            employee.setEmployeePassport(null);
            employeeRepository.save(employee);
        }
    }

    @Override
    public Map<Long, Employee> getEmployeesWithDeprivePassports() {
        var passports = employeePassportService.getAllDeprivePassports();
        Map<Long, Employee> historyList = new HashMap<>();
        for (EmployeePassport eP : passports) {
            var e = getById(eP.getPreviousPassportId());
            historyList.put(eP.getId(), getById(eP.getPreviousPassportId()));
        }
        return historyList;
    }
    /**
     * @apiNote Homework_15. Get passport history who deprived.
     */
    public Map<String, String> getHistoryPassport() {
        var passports = employeePassportService.getAllDeprivePassports();
        Map<String, String> history = new HashMap<>();
        for (EmployeePassport eP : passports) {
            var e = getById(eP.getPreviousPassportId());
            String key = "Passport id: " + eP.getId();
            String entity = "Employee id: " + e.getId() + "; Name: " + e.getName();
            history.put(key, entity);
        }
        return history;
    }
    /**
     * @apiNote Homework_15. Get employee history with deprived passports.
     */
    @Override
    public Map<String, List<String>> getEmployeePassportHistory(Integer employeeId) {
        var pass = getStringsFormatPassportInfo(employeeId);
        Map<String, List<String>> list = new HashMap<>();
        var formEmployeeInfo = String.format("Employee with id: %s; History had passports:", employeeId);
        list.put(formEmployeeInfo, pass);
        return list;
    }
    /**
     * @apiNote Inside method getEmployeePassportHistory().
     */
    private List<String> getStringsFormatPassportInfo(Integer employeeId) {
        var passports = employeePassportService.getAllDeprivePassports();
        List<String> pass = new ArrayList<>();
        for (EmployeePassport passport : passports) {
            if (passport.getPreviousPassportId() == employeeId) {
                var formPassportInfo = String.format("Passport id: %s; series/number: %s %s",
                        passport.getId(), passport.getSeries(), passport.getNumber());
                pass.add(formPassportInfo);
            }
        }
        if(pass.isEmpty()){
            throw new PassportNoOneFindException();
        }
        return pass;
    }


    private boolean comparisonEmployee(Employee employee) {
        getAll().stream().filter(emp ->
                        Objects.equals(emp.getName(), employee.getName()) &&
                                Objects.equals(emp.getCountry(), employee.getCountry()) &&
                                Objects.equals(emp.getEmail(), employee.getEmail()) &&
                                Objects.equals(emp.getGender(), employee.getGender()))
                .findFirst()
                .ifPresent(emp -> {
                    throw new EmployeeContainsException();
                });
        return true;
    }


    /* public boolean isValid(Employee employee) {
        String regex = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(employee.getPhone());
        boolean isFound = matcher.find();
        if (isFound) {
            System.out.println("Number is valid");
            return true;
        } else {
            System.out.println("Number is invalid");
            return false;
        }
    }*/

    /*public boolean isVodafone(Employee employee) {
        String regex = "^[0][9][5]{1}[0-9]{7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(employee.getPhone());
        boolean isFound = matcher.find();
        if (isFound) {
            System.out.println("Number is Vodafone");
            return true;
        } else {
            System.out.println("Number is not Vodafone");
            return false;
        }
    }*/

}
