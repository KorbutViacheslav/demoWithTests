package com.example.demowithtests.repository;

import com.example.demowithtests.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reservation, Long> {
}
