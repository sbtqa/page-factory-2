package ru.sbtqa.tag.pagefactory.aspects;

import static ru.sbtqa.tag.pagefactory.optional.PickleStepCustom.NON_CRITICAL;
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

import java.util.ArrayList;
import java.util.List;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;

@Aspect
public class CriticalStepAspect {

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
        PickleStepCustom pickleStepCustom = new PickleStepCustom(step);
        return joinPoint.proceed(new Object[]{featurePath, pickleStepCustom});
    }

    @Around(value = "argumentOffset(arguments, stepDefinition, featurePath, step, localizedXStreams)",
            argNames = "joinPoint,arguments,stepDefinition,featurePath,step,localizedXStreams")
    public Object argumentOffset(ProceedingJoinPoint joinPoint, List<Argument> arguments, StepDefinition stepDefinition,
                                 String featurePath, PickleStep step, LocalizedXStreams localizedXStreams) throws Throwable {

        List<Argument> shiftedArguments = new ArrayList<>();
        for (Argument argument : arguments) {
            String argValue = argument.getVal();

            if (isReplaceableArgument(step, argument)) {
                int offset = step.getText().indexOf(argValue) + NON_CRITICAL.length();
                argument = new Argument(offset, argValue);
            }
            shiftedArguments.add(argument);
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
