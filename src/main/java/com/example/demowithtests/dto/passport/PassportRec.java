package com.example.demowithtests.dto.passport;

import com.example.demowithtests.domain.Photo;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record PassportRec(@Hidden
                          Long id,
                          @Hidden
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
                          @Hidden
                          Date handDate,
                          @Hidden
                          LocalDateTime expireDate,
                          @Hidden
                          Boolean isHanded,
                          @Hidden
                          Photo photo) {
    public PassportRec {
        uuid = UUID.randomUUID().toString();
        isHanded = Boolean.FALSE;
    }
}
