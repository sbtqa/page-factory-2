package ru.sbtqa.tag.pagefactory.aspects.report;

import cucumber.api.Result;
import cucumber.api.event.Event;
import cucumber.api.event.TestStepFinished;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.allure.ErrorHandler;
import ru.sbtqa.tag.pagefactory.environment.Environment;

@Aspect
public class ErrorHandlerAspect {

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepFinished(Event event) {
        return event instanceof TestStepFinished;
    }

    @Around("sendStepFinished(event)")
    public void sendStepFinished(ProceedingJoinPoint joinPoint, Event event) throws Throwable {
        TestStepFinished testStepFinished = (TestStepFinished) event;
        if (testStepFinished.result.getStatus() == Result.Type.FAILED
                && !Environment.isDriverEmpty()) {
//            ErrorHandler.attachError(testStepFinished.result.getError().getMessage(), testStepFinished.result.getError());
            ErrorHandler.attachError(testStepFinished.result.getError());
            ErrorHandler.attachScreenshot();
        }
        joinPoint.proceed();
    }
}
