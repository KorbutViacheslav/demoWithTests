package com.example.demowithtests.util.config.mapstruct;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.dto.employee.EmployeeReadRec;
import com.example.demowithtests.dto.employee.EmployeeRec;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "id", ignore = true)
    Employee toEmployee(EmployeeRec dto);

    EmployeeRec toEmployeeDto(Employee entity);

    EmployeeReadRec toReadDto(Employee employee);

    List<EmployeeReadRec> toListEmployeeReadDto(List<Employee> employees);
}




