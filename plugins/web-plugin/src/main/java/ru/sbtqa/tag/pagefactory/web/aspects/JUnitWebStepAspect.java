package ru.sbtqa.tag.pagefactory.web.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import ru.sbtqa.tag.pagefactory.allure.JunitReporter;

@Aspect
public class JUnitWebStepAspect {

    @Around("execution(public * ru.sbtqa.tag.pagefactory.web.junit.WebStepsImpl.*(..))")
    public Object handleStep(ProceedingJoinPoint joinPoint) throws Throwable {
        return JunitReporter.handleStep(joinPoint);
    }

}
