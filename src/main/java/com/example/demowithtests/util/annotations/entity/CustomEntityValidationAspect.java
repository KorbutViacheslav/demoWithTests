package com.example.demowithtests.util.annotations.entity;

import com.example.demowithtests.domain.Employee;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class CustomEntityValidationAspect {

    @Pointcut("@annotation(com.example.demowithtests.util.annotations.entity.ActivateCustomAnnotations) && within(com.example.demowithtests.service.*)")
    public void callAtAnnotationActivator() {
    }

    @Before("callAtAnnotationActivator()")
    public void makeValid(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ActivateCustomAnnotations annotation = signature.getMethod().getAnnotation(ActivateCustomAnnotations.class);
        List<Class<?>> annotations = Arrays.asList(annotation.value());
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Employee) {
                for (Field field : arg.getClass().getDeclaredFields()) {
                    if (annotations.contains(ToLowerCase.class) && field.isAnnotationPresent(ToLowerCase.class)) {
                        setLowerCase(arg, field);
                    }
                    if (annotations.contains(Name.class) && field.isAnnotationPresent(Name.class)) {
                        setFormattedName(arg, field);
                    }
                }
            }
        }
    }

    private void setFormattedName(Object arg, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Object value = field.get(arg);
        if (value instanceof String) {
            field.set(arg, toNameFormat((String) value));
        }
    }

    private String toNameFormat(String name) {
        return StringUtils.capitalize(name.trim().toLowerCase());
    }

    private void setLowerCase(Object arg, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Object value = field.get(arg);
        if (value instanceof String) {
            field.set(arg, ((String) value).toLowerCase());
        }
    }
}
