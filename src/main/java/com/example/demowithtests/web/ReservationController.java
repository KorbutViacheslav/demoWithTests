package com.example.demowithtests.web;

import com.example.demowithtests.domain.Reservation;
import com.example.demowithtests.service.work_place.ReserveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/reserve",produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reservation", description = "Reservation API")
public class ReservationController {
    private final ReserveService reserveService;

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable Long id){
        return reserveService.getReservationById(id);
    }

    @PatchMapping("/up")
    public ResponseEntity<String> updateReserves(){
        String massage = "Reservations update status!";
        reserveService.updateReservationStatus();
        return ResponseEntity.ok().body(massage);
    }
}
