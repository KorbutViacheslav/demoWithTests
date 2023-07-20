package com.example.demowithtests.service.work_place;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Reservation;
import com.example.demowithtests.repository.EmployeeRepository;
import com.example.demowithtests.repository.ReserveRepository;
import com.example.demowithtests.repository.WorkPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReserveService {

    private final ReserveRepository reserveRepository;
    private final WorkPlaceRepository workPlaceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Reservation create(Integer employeeId, Long workPlaceId) {
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee absent"));
        if (employee.getReservations().size() >= 3) {
            throw new RuntimeException("Employee dont make more work places");
        }
        var workPlace = workPlaceRepository.findById(workPlaceId)
                .orElseThrow(() -> new NotFoundException("An employee cannot have more than 3 jobs"));
        if (!workPlace.getIsActive()) {
            throw new RuntimeException("Work place busy!");
        }
        workPlace.setIsActive(Boolean.FALSE);
        workPlaceRepository.save(workPlace);

        var reservation = new Reservation();
        reservation.setEmployee(employee);
        reservation.setWorkPlace(workPlace);
        reservation.setIsActive(Boolean.TRUE);
        reservation.setStartTime(LocalDateTime.now());
        reservation.setEndTime(LocalDateTime.now().plusMinutes(1));
        return reserveRepository.save(reservation);
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reserveRepository.findById(id).orElseThrow(() -> new NotFoundException("Reservation absent!"));
    }

    @Override
    public List<Reservation> getAllReservation() {
        List<Reservation> list = reserveRepository.findAll();
        if (list.isEmpty()) {
            throw new NotFoundException("No one reservation not found");
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

    private List<Reservation> getReservations() {
        var reservations = reserveRepository.findAll();
        if (reservations.isEmpty()) {
            throw new NotFoundException("No one reservation not found!");
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
        workPlace.setIsActive(Boolean.TRUE);
        workPlaceRepository.save(workPlace);
    }
}
