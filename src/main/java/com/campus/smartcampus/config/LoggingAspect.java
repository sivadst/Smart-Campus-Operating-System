package com.campus.smartcampus.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {}

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointcut() {}

    @Around("controllerPointcut() || servicePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.debug("Enter: {}.{}()", className, methodName);
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            log.debug("Exit: {}.{}() - {}ms", className, methodName, elapsed);
            return result;
        } catch (Exception e) {
            log.error("Exception in {}.{}(): {}", className, methodName, e.getMessage());
            throw e;
        }
    }
}
