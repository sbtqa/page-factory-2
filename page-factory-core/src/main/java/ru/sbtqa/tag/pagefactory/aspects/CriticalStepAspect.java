package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.runtime.Argument;
import cucumber.runtime.StepDefinition;
import cucumber.runtime.xstream.LocalizedXStreams;
import gherkin.pickles.PickleStep;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.optional.ArgumentCustom;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Aspect
public class CriticalStepAspect {

    private static final String NON_CRITICAL = "? ";

    @Pointcut(value = "execution(* cucumber.runtime.RuntimeGlue.stepDefinitionMatch(..)) && args(featurePath, step,..)",
            argNames = "featurePath,step")
    public void addSignOfCritically(String featurePath, PickleStep step) {
    }

    @Pointcut(value = "call(cucumber.runtime.StepDefinitionMatch.new(..)) && args(arguments, stepDefinition, featurePath, step, localizedXStreams,..)",
            argNames = "arguments,stepDefinition,featurePath,step,localizedXStreams")
    public void argumentOffset(List<Argument> arguments, StepDefinition stepDefinition, String featurePath, PickleStep step, LocalizedXStreams localizedXStreams) {
    }

    @Pointcut(value = "execution(* cucumber.runtime.snippets.SnippetGenerator.getSnippet(..)) && args(step,..)")
    public void getSnippet(PickleStep step) {
    }

    @Around(value = "addSignOfCritically(featurePath, step)", argNames = "joinPoint,featurePath,step")
    public Object addSignOfCritically(ProceedingJoinPoint joinPoint, String featurePath, PickleStep step) throws Throwable {
        PickleStepCustom newStep;
        String stepText = step.getText();

        if (stepText.startsWith(NON_CRITICAL)) {
            String replacedSteps = stepText.replaceFirst("\\" + NON_CRITICAL, "");
            newStep = new PickleStepCustom(step, replacedSteps, false);
        } else {
            newStep = new PickleStepCustom(step, stepText, true);
        }
        return joinPoint.proceed(new Object[]{featurePath, newStep});
    }


    @Around(value = "argumentOffset(arguments, stepDefinition, featurePath, step, localizedXStreams)",
            argNames = "joinPoint,arguments,stepDefinition,featurePath,step,localizedXStreams")
    public Object argumentOffset(ProceedingJoinPoint joinPoint, List<Argument> arguments, StepDefinition stepDefinition,
                                 String featurePath, PickleStep step, LocalizedXStreams localizedXStreams) throws Throwable {
        if (step.getClass().equals(PickleStepCustom.class)) {
            List<Argument> shiftedArguments = new ArrayList<>();
            boolean isCritical = ((PickleStepCustom) step).isCritical();

            for (Argument argument : arguments) {
                Argument arg = argument;
                if (arg.getVal() != null) {
                    if (argument.getClass().equals(ArgumentCustom.class) && ((ArgumentCustom) argument).isModified()) {
                        if (isCritical) {
                            arg = new ArgumentCustom(argument.getOffset() - NON_CRITICAL.length(), argument.getVal(), false);
                        }
                    } else {
                        if (!isCritical) {
                            arg = new ArgumentCustom(argument.getOffset() + NON_CRITICAL.length(), argument.getVal(), true);
                        }
                    }
                }
                shiftedArguments.add(arg);
            }
            return joinPoint.proceed(new Object[]{shiftedArguments, stepDefinition, featurePath, step, localizedXStreams});
        }
        return joinPoint.proceed(new Object[]{arguments, stepDefinition, featurePath, step, localizedXStreams});
    }

    @Around("getSnippet(step)")
    public String getSnippet(ProceedingJoinPoint joinPoint, PickleStep step) throws Throwable {
        String stepText = step.getText();
        if (stepText.startsWith(NON_CRITICAL)) {
            String jpResult = (String) joinPoint.proceed();

            String replaceableTextRegExp = "\\\\\\\\\\" + stepText;
            String replaced = stepText.replaceFirst("\\" + NON_CRITICAL, "");
            return jpResult.replaceFirst(replaceableTextRegExp, replaced);
        } else {
            return (String) joinPoint.proceed();
        }
    }
}
