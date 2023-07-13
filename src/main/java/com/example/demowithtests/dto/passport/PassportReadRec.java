package com.example.demowithtests.dto.passport;

import com.example.demowithtests.dto.photo.PhotoRec;

import java.time.LocalDateTime;
import java.util.Date;

public record PassportReadRec(String uuid,
                              String series,
                              String number,
                              String bodyHanded,
                              Date handDate,
                              LocalDateTime expireDate,
                              Boolean isHanded,
                              PhotoRec photoRec) {
}
