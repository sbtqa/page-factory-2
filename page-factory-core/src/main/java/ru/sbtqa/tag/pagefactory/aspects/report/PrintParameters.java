package ru.sbtqa.tag.pagefactory.aspects.report;

import cucumber.api.PickleStepTestStep;
import cucumber.api.TestStep;
import cucumber.api.event.Event;
import cucumber.api.event.TestStepFinished;
import gherkin.pickles.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PrintParameters {

    private final ThreadLocal<List<Argument>> testArguments = ThreadLocal.withInitial(ArrayList::new);

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepFinished(Event event) {
        return event instanceof TestStepFinished;
    }

    @Before("sendStepFinished(event)")
    public void beforeSendStepFinished(JoinPoint joinPoint, Event event) {
        TestStep testStep = ((TestStepFinished) event).testStep;
        if (testStep instanceof PickleStepTestStep) {
            this.testArguments.set(((PickleStepTestStep) testStep).getStepArgument());
            addAllureArguments();
        }
    }

    @After("execution(* cucumber.runtime.formatter.PrettyFormatter.printStep(..))")
    public void printStep(JoinPoint joinPoint) {
        PickleStepTestStep step = joinPoint.getArgs().length > 0
                && joinPoint.getArgs()[0] instanceof PickleStepTestStep
                ? (PickleStepTestStep) joinPoint.getArgs()[0] : null;

        if (step != null) {
            for (Argument argument : step.getPickleStep().getArgument()) {
                if (argument instanceof PickleString) {
                    printPickleString((PickleString) argument);
                } else {
                    printPickleTable((PickleTable) argument);
                }
            }
        }
    }

    private void addAllureArguments() {
        Argument pickleString = this.testArguments.get().stream()
                .filter(PickleString.class::isInstance).findFirst().orElse(null);

        if (pickleString != null) {
            Allure.getLifecycle().updateStep(stepResult ->
                    stepResult.setAttachments(new ArrayList<>()));
            this.textAttachment(((PickleString) pickleString).getContent());
        }
    }

    private void printPickleString(PickleString pickleString) {
        if (pickleString != null) {
            System.out.println("      \"\"\"");
            System.out.println("      " + pickleString.getContent());
            System.out.println("      \"\"\"");
        }
    }

    private void printPickleTable(PickleTable pickleTable) {
        for (PickleRow row : pickleTable.getRows()) {
            System.out.print("      | ");
            for (PickleCell cell : row.getCells()) {
                System.out.print(cell.getValue() + " | ");
            }
            System.out.println();
        }
    }

    @Attachment(value = "{text}", type = "text/plain")
    private String textAttachment(String text) {
        return text;
    }
}
