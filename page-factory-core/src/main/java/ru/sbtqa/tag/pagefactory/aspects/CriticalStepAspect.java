package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.Scenario;
import cucumber.api.TestStep;
import cucumber.api.event.TestStepStarted;
import cucumber.runner.EventBus;
import cucumber.runtime.Argument;
import cucumber.runtime.StepDefinition;
import cucumber.runtime.xstream.LocalizedXStreams;
import gherkin.pickles.PickleStep;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;

import java.util.ArrayList;
import java.util.List;

@Aspect
public class CriticalStepAspect {

    private static final String NON_CRITICAL = "? ";

    @Pointcut(value = "execution(* cucumber.runtime.RuntimeGlue.stepDefinitionMatch(..)) && args(featurePath, step,..)")
    public void addSignOfCritically(String featurePath, PickleStep step) {
    }

    @Pointcut(value = "call(cucumber.runtime.StepDefinitionMatch.new(..)) && args(arguments, stepDefinition, featurePath, step, localizedXStreams,..)")
    public void argumentOffset(List<Argument> arguments, StepDefinition stepDefinition,
                               String featurePath, PickleStep step, LocalizedXStreams localizedXStreams) {
    }

    @Pointcut(value = "execution(* cucumber.runtime.snippets.SnippetGenerator.getSnippet(..)) && args(step,..) && if()")
    public static boolean getSnippet(PickleStep step) {
        return step.getText().startsWith(NON_CRITICAL);
    }

    @Around(value = "addSignOfCritically(featurePath, step)")
    public Object addSignOfCritically(ProceedingJoinPoint joinPoint, String featurePath, PickleStep step) throws Throwable {
        boolean isCritical = true;
        String stepText = step.getText();

        if (stepText.startsWith(NON_CRITICAL)) {
            stepText = stepText.replaceFirst("\\" + NON_CRITICAL, "");
            isCritical = false;
        }
        PickleStepCustom pickleStepCustom = new PickleStepCustom(step, stepText, isCritical);
        return joinPoint.proceed(new Object[]{featurePath, pickleStepCustom});
    }

    @Around(value = "argumentOffset(arguments, stepDefinition, featurePath, step, localizedXStreams)",
            argNames = "joinPoint,arguments,stepDefinition,featurePath,step,localizedXStreams")
    public Object argumentOffset(ProceedingJoinPoint joinPoint, List<Argument> arguments, StepDefinition stepDefinition,
                                 String featurePath, PickleStep step, LocalizedXStreams localizedXStreams) throws Throwable {

        List<Argument> shiftedArguments = new ArrayList<>();
        for (Argument argument : arguments) {
            Argument arg = argument;
            String argValue = arg.getVal();

            if (isReplaceableArgument(step, arg)) {
                int offset = step.getText().indexOf(argValue) + NON_CRITICAL.length();
                arg = new Argument(offset, argValue);
            }
            shiftedArguments.add(arg);
        }
        return joinPoint.proceed(new Object[]{shiftedArguments, stepDefinition, featurePath, step, localizedXStreams});
    }

    private boolean isReplaceableArgument(PickleStep step, Argument argument) {
        return argument.getVal() != null
                && step instanceof PickleStepCustom && !((PickleStepCustom) step).isCritical();
    }

    @Around("getSnippet(step)")
    public String getSnippet(ProceedingJoinPoint joinPoint, PickleStep step) throws Throwable {
        String stepText = step.getText();
        String jpResult = (String) joinPoint.proceed();

        String replaceableTextRegExp = "\\\\\\\\\\" + stepText.substring(0, stepText.indexOf("\""));
        String replaced = stepText.replaceFirst("\\" + NON_CRITICAL, "");
        replaced = replaced.substring(0, replaced.indexOf("\""));
        return jpResult.replaceFirst(replaceableTextRegExp, replaced);
    }
}
