package com.fidelity.mts.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * AOP Aspect for audit logging across all controller and service methods.
 * Satisfies NFR-04: Audit Trail requirement.
 */
@Aspect
@Component
public class AuditLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditLoggingAspect.class);

    /**
     * Pointcut for all controller methods.
     */
    @Pointcut("execution(* com.fidelity.mts.controller..*(..))")
    public void controllerMethods() {}

    /**
     * Pointcut for all service methods.
     */
    @Pointcut("execution(* com.fidelity.mts.servcie..*(..))")
    public void serviceMethods() {}

    /**
     * Around advice: logs method entry, exit, execution time.
     */
    @Around("controllerMethods() || serviceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());

        logger.info("AUDIT >> Entering {}.{}() with args: {}", className, methodName, args);

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsed = System.currentTimeMillis() - start;

        logger.info("AUDIT << Exiting {}.{}() [{}ms]", className, methodName, elapsed);

        return result;
    }

    /**
     * AfterThrowing advice: logs exceptions thrown by service/controller methods.
     */
    @AfterThrowing(pointcut = "controllerMethods() || serviceMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        logger.error("AUDIT !! Exception in {}.{}(): {} — {}", className, methodName,
                ex.getClass().getSimpleName(), ex.getMessage());
    }
}
