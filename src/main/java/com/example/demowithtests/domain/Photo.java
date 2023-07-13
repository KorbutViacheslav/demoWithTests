package com.example.demowithtests.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Lob
    private byte[] data;

    @OneToOne(mappedBy = "photo")
    private EmployeePassport passport;

}