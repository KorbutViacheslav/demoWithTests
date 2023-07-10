package com.example.demowithtests.domain;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "passports")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeePassport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid = UUID.randomUUID().toString();
    private String series;
    private String number;
    private String bodyHanded;
    private Date handDate;
    private LocalDateTime expireDate;
    @Column(name = "is_handed")
    private Boolean isHanded = Boolean.FALSE;
}
