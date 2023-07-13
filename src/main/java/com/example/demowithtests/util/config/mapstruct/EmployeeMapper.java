package com.example.demowithtests.util.config.mapstruct;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.dto.employee.EmployeeReadRec;
import com.example.demowithtests.dto.employee.EmployeeRec;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(source = "entity.employeePassport.photo", target = "passportReadRec.photoRec")
    EmployeeRec toEmployeeDto(Employee entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "dto.passportReadRec.photoRec", target = "employeePassport.photo")
    Employee toEmployee(EmployeeRec dto);

    @Mapping(source = "employee.employeePassport.photo", target = "passportReadRec.photoRec")
    EmployeeReadRec toReadRec(Employee employee);

    List<EmployeeReadRec> toListEmployeeReadDto(List<Employee> employees);
}




