package com.example.glibrary.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.glibrary.controller..*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Вызов метода: " + joinPoint.getSignature());
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        logger.error("Ошибка в методе: " + joinPoint.getSignature(), ex);
    }
}
