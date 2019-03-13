package ru.sbtqa.tag.api.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import ru.sbtqa.tag.pagefactory.allure.JunitReporter;

@Aspect
public class JUnitApiStepAspect {

    @Around("execution(public * ru.sbtqa.tag.api.junit.ApiStepsImpl.*(..))")
    public Object handleStep(ProceedingJoinPoint joinPoint) throws Throwable {
        return JunitReporter.handleStep(joinPoint);
    }
}
