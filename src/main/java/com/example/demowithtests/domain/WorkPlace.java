package com.example.demowithtests.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "work_places")
public class WorkPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean airCondition = Boolean.TRUE;

    private Boolean coffeeMachine = Boolean.TRUE;

    private Boolean isActive = Boolean.TRUE;

    @ManyToMany(mappedBy = "workPlaces")
    @JsonIgnore
    private Set<Employee> employee;

}