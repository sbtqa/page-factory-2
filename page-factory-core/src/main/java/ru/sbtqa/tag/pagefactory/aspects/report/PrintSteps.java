package ru.sbtqa.tag.pagefactory.aspects.report;

import cucumber.api.HookTestStep;
import cucumber.api.PickleStepTestStep;
import cucumber.api.event.Event;
import cucumber.api.event.TestStepStarted;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.optional.PickleStepTag;

@Aspect
public class PrintSteps {

    private String previous = "";

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepStarted(Event event) {
        return event instanceof TestStepStarted
                && !(((TestStepStarted) event).testStep instanceof HookTestStep);
    }

    @Around("sendStepStarted(event)")
    public void sendStepStarted(ProceedingJoinPoint joinPoint, Event event) throws Throwable {
        PickleStepTestStep testStep = (PickleStepTestStep) ((TestStepStarted) event).testStep;
        String stepText = testStep.getStepText();
        if (!previous.equals(stepText)
                && testStep.getPickleStep() instanceof PickleStepTag
                && !((PickleStepTag)testStep.getPickleStep()).isSkipped()) {
            System.out.println("    * " + stepText);
            previous = stepText;
        }

        joinPoint.proceed();
    }
}
