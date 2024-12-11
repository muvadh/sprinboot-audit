package com.gridsig.audits.aspect;

import org.aspectj.lang.annotation.After;
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

    @Pointcut("execution(* com.gridsig.audits.controller.*.*(..))")
    public void controllerMethods() {
        // Pointcut for all methods in the controller package
    }

    @Before("controllerMethods()")
    public void logRequest() {
        logger.info("Logging before request...");
    }

    @After("controllerMethods()")
    public void logResponse() {
        logger.info("Logging after response...");
    }
}
