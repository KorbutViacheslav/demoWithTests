package com.example.demowithtests.util.annotations.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Log4j2
@Aspect
@Component
public class LoggingServiceClassesAspect {
    private static final String BLUE_COLOR = "\u001B[34m";
    private static final String RESET_COLOR = "\u001B[0m";

    @Pointcut("execution(public * com.example.demowithtests.service.*Service.*(..))")
    public void callAtMyServicesPublicMethods() {
    }

    @Around("callAtMyServicesPublicMethods() && args(returningValue)")
    public Object addLoggingAround(ProceedingJoinPoint joinPoint, Object returningValue) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.debug(BLUE_COLOR + "Service: {} - start." + RESET_COLOR, methodName);
        try {
            Object result = joinPoint.proceed();
            Object outputValue = Optional.ofNullable(result)
                    .map(value -> value instanceof byte[] ? "File as byte[]" : value)
                    .map(value -> value instanceof Collection ? "Collection size - " + ((Collection<?>) value).size() : value)
                    .orElse(null);

            log.debug(BLUE_COLOR + "Service: {} - end." + (outputValue != null ? " Returns - " + outputValue : "") + RESET_COLOR, methodName);
            return result;

        } catch (Throwable ex) {
            log.debug(BLUE_COLOR + "Throwing {} in {} " + RESET_COLOR, ex, methodName);
            throw ex;
        }
    }
}
