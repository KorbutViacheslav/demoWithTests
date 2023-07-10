package com.example.demowithtests.dto.passport;

import com.example.demowithtests.util.annotations.entity.Name;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record PassportRec(Long id,
                          String uuid,

                          @NotNull
                          @Size(min = 2, max = 2, message = "Series must be 2 characters long")
                          @Schema(description = "Passport series.", example = "MN")
                          String series,

                          @NotNull
                          @Size(min = 6, max = 6, message = "Number must be 6 characters long")
                          @Schema(description = "Passport number.", example = "258258")
                          String number,

                          @NotNull
                          @Schema(description = "Passport issued by.", example = "Ukraine inner ministry department in Kharkiv.")
                          String bodyHanded,
                          Date handDate,
                          LocalDateTime expireDate,
                          Boolean isHanded) {
    public PassportRec {
        uuid = UUID.randomUUID().toString();
        expireDate = LocalDateTime.now().plusYears(10);
        isHanded = Boolean.FALSE;
    }
}
