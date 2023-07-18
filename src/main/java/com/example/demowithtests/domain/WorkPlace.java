package com.example.demowithtests.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Builder
@Getter
@Setter
@ToString
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

    @ManyToMany(mappedBy = "workPlaces",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Employee> employee = new HashSet<>();

}