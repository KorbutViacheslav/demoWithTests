package com.example.demowithtests.util.annotations.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
/**
 * @author Viacheslav Korbut
 * @implNote
 * home task №11. Implement around aspect.
 * Deleted before and after aspect and added to around.
 * Added throw log in around aspect.
 * Fix logic after log.
 * Add static color.
 */

@Log4j2
@Aspect
@Component
public class LoggingControllerClassesAspect {
    private static final String GREEN_COLOR = "\u001B[32m";
    private static final String RESET_COLOR = "\u001B[0m";

    @Pointcut("execution(public * com.example.demowithtests.web.EmployeeController.*(..))")
    public void callAtMyControllersPublicMethods() {
    }

    @Around("callAtMyControllersPublicMethods()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("{}Controller: {} - start.{}", GREEN_COLOR, methodName, RESET_COLOR);
        try {
            Object result = joinPoint.proceed();
            log.info("{}Controller: {} - end.{}", GREEN_COLOR, methodName, RESET_COLOR);
            return result;
        } catch (Throwable ex) {
            log.info("{}Throw {} in {}", GREEN_COLOR, ex, methodName);
            throw ex;
        }
    }
}
