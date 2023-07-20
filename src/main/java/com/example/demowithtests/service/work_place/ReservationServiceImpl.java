package com.example.demowithtests.service.work_place;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Reservation;
import com.example.demowithtests.domain.WorkPlace;
import com.example.demowithtests.repository.EmployeeRepository;
import com.example.demowithtests.repository.ReserveRepository;
import com.example.demowithtests.repository.WorkPlaceRepository;
import com.example.demowithtests.util.exception.employee.ResourceNotFoundException;
import com.example.demowithtests.util.exception.reservation.ReservationNotFoundException;
import com.example.demowithtests.util.exception.workplace.WorkPlaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReserveService {

    private final ReserveRepository reserveRepository;
    private final WorkPlaceRepository workPlaceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Reservation create(Integer employeeId, Long workPlaceId) {
        var employee = getEmployee(employeeId);
        var workPlace = getWorkPlace(workPlaceId);
        workPlace.setIsFree(Boolean.FALSE);
        workPlaceRepository.save(workPlace);

        var reservation = Reservation.builder()
                .employee(employee)
                .workPlace(workPlace)
                .isActive(Boolean.TRUE)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(1))
                .build();
        return reserveRepository.save(reservation);
    }


    @Override
    public Reservation getReservationById(Long id) {
        return reserveRepository.findById(id).orElseThrow(ReservationNotFoundException::new);
    }

    @Override
    public List<Reservation> getAllReservation() {
        List<Reservation> list = reserveRepository.findAll();
        if (list.isEmpty()) {
            throw new ReservationNotFoundException();
        }
        return list;
    }

    @Override
    public void deleteReservationById(Long id) {
        reserveRepository.deleteById(id);
    }

    @Override
    public void updateReservationStatus() {
        var listReservations = getReservations();
        var now = LocalDateTime.now();
        for (Reservation reservation : listReservations) {
            var endTime = reservation.getEndTime();
            if (endTime != null && now.isAfter(endTime)) {
                updateWorkPlace(reservation);
                updateEmployee(reservation);
                reservation.setIsActive(Boolean.FALSE);
                reserveRepository.save(reservation);
            }
        }
    }

    /**
     * @apiNote Inside method for updateReservationStatus().
     */
    private List<Reservation> getReservations() {
        var reservations = reserveRepository.findAll();
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException();
        }
        return reservations;
    }

    private void updateEmployee(Reservation reservation) {
        var employee = reservation.getEmployee();
        reservation.setEmployee(null);
        employee.getReservations().remove(reservation);
        employeeRepository.save(employee);
    }

    private void updateWorkPlace(Reservation reservation) {
        var workPlace = reservation.getWorkPlace();
        workPlace.setIsFree(Boolean.TRUE);
        workPlaceRepository.save(workPlace);
    }

    /**
     * @apiNote Inside method for create().
     */

    private WorkPlace getWorkPlace(Long workPlaceId) {
        var workPlace = workPlaceRepository.findById(workPlaceId)
                .orElseThrow(WorkPlaceNotFoundException::new);
        if (!workPlace.getIsFree()) {
            throw new IllegalStateException("Work place busy!");
        }
        return workPlace;
    }

    private Employee getEmployee(Integer employeeId) {
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(ResourceNotFoundException::new);
        if (employee.getReservations().size() >= 3) {
            throw new IllegalStateException("Employee dont make more work places");
        }
        return employee;
    }
}
