package com.example.demowithtests.dto.passport;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record PassportReadRec(String uuid,
                              String series,
                              String number,
                              String bodyHanded,
                              Date handDate,
                              LocalDateTime expireDate,
                              Boolean isHanded) {
    public PassportReadRec {
        uuid = UUID.randomUUID().toString();
        isHanded = Boolean.FALSE;
    }
}
