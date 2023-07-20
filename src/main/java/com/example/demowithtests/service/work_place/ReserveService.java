package com.example.demowithtests.service.work_place;

import com.example.demowithtests.domain.Reservation;

import java.util.List;

public interface ReserveService {

    Reservation create(Integer employeeId, Long workPlaceId);

    Reservation getReservationById(Long id);

    List<Reservation> getAllReservation();

    void deleteReservationById(Long id);

    void updateReservationStatus();
}
