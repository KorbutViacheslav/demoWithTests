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
    @Mapping(source = "passportRec.photoRec", target = "photo")
    EmployeePassport toEmployeePassport(PassportRec passportRec);
    @Mapping(source = "employeePassport.photo", target = "photoRec")
    PassportRec toPassportRec(EmployeePassport employeePassport);
    @Mapping(source = "employeePassport.photo", target = "photoRec")
    PassportReadRec toPassportReadRec(EmployeePassport employeePassport);
    @Mapping(source = "passportRec.photoRec", target = "photoRec")
    PassportReadRec toPassportReadRec(PassportRec passportRec);

    List<PassportReadRec> toListPassportReadRec(List<EmployeePassport> list);
}
