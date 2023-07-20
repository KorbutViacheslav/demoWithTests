package com.example.demowithtests.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private Employee employee;


    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isActive = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "work_place_id")
    private WorkPlace workPlace;
}
