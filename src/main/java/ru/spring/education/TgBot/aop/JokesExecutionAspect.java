package ru.spring.education.TgBot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class JokesExecutionAspect {

    @Pointcut("execution(public * ru.spring.education.TgBot.Controller.Controller.postJoke(..))")
    public void callPostJoke() {}

    @Around("callPostJoke()")
    public Object aroundPostJoke(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Start with user {} {} in {} and end {}", SecurityContextHolder.getContext().getAuthentication().getName(), proceedingJoinPoint.getSignature().getName(), startTime, endTime);
        return result;
    }
}