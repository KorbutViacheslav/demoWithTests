package com.example.demowithtests.dto.addres;

import java.time.Instant;
import java.util.Date;

public record AddressRec(Long id,
                         Boolean addressHasActive,
                         String country,
                         String city,
                         String street,
                         Date date) {
    public AddressRec {
        addressHasActive = Boolean.TRUE;
        date = Date.from(Instant.now());
    }
}
