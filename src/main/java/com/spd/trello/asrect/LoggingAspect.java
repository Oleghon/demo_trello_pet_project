package com.spd.trello.asrect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(com.spd.trello*)" +
            " || within(com.spd.trello.service.*)" +
            " || within(com.spd.trello.controller.*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Around("@annotation(com.spd.trello.annotation.Debug)")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        var targetClass = pjp.getSignature().getDeclaringType();
        var log = LoggerFactory.getLogger(targetClass);
        var args = Arrays.stream(pjp.getArgs()).map(Object::toString).collect(joining(","));
        var methodName = pjp.getSignature().getName();

        if (log.isDebugEnabled()) {
            log.debug("{}({}): invoked", methodName, args);
        }

        var result = pjp.proceed();
        if (log.isDebugEnabled()) {
            log.debug("{}({}): returning; result={}", methodName, args, result);
        }
        return result;
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        var targetClass = joinPoint.getSignature().getDeclaringType();
        var log = LoggerFactory.getLogger(targetClass);
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
    }
}
