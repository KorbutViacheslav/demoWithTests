package com.example.demowithtests.util.config.mapstruct;

import com.example.demowithtests.domain.EmployeePassport;
import com.example.demowithtests.dto.passport.PassportReadRec;
import com.example.demowithtests.dto.passport.PassportRec;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PassportMapper {
    PassportMapper INSTANCE = Mappers.getMapper(PassportMapper.class);

    @Mapping(target = "id", ignore = true)
    EmployeePassport toEmployeePassport(PassportRec passportRec);

    PassportRec toPassportRec(EmployeePassport employeePassport);

    PassportReadRec toPassportReadRec(EmployeePassport employeePassport);

    List<PassportReadRec> toListPassportReadRec(List<EmployeePassport> list);
}
