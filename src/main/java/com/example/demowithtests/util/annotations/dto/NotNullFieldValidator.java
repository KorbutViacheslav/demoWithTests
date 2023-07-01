package com.example.demowithtests.util.annotations.dto;

import com.example.demowithtests.dto.EmployeeRec;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
/**
 * @author Viacheslav Korbut
 * @implNote
 * home task №11. Implement validator.
 * Fields name,country and gender not be null.
 */

public class NotNullFieldValidator implements ConstraintValidator<NotNullField, EmployeeRec> {
    @Override
    public void initialize(NotNullField constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(EmployeeRec employeeRec, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.hasText(employeeRec.name())
                && StringUtils.hasText(employeeRec.country())
                && employeeRec.gender() != null;
    }
}
