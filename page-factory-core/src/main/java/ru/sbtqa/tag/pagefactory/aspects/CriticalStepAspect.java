package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.runtime.Argument;
import cucumber.runtime.StepDefinition;
import cucumber.runtime.xstream.LocalizedXStreams;
import gherkin.pickles.PickleStep;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.optional.ArgumentCustom;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;

@Aspect
public class CriticalStepAspect {

    private static final String NON_CRITICAL = "? ";

    @Pointcut("execution(* cucumber.runtime.RuntimeGlue.stepDefinitionMatch(..))  && args(featurePath, step,..)")
    public void addSignOfCriticality(String featurePath, PickleStep step) {
    }

    @Around("addSignOfCriticality(featurePath, step)")
    public Object addSignOfCriticality(ProceedingJoinPoint joinPoint, String featurePath, PickleStep step) throws Throwable {
        PickleStepCustom newStep;
        String stepText = step.getText();

        if (stepText.startsWith(NON_CRITICAL)) {
            String replacedSteps = stepText.replaceFirst("\\" + NON_CRITICAL, "");
            newStep = new PickleStepCustom(replacedSteps, step.getArgument(), step.getLocations(), false);
        } else {
            newStep = new PickleStepCustom(stepText, step.getArgument(), step.getLocations(), true);
        }
        return joinPoint.proceed(new Object[]{featurePath, newStep});
    }

    @Pointcut("call(cucumber.runtime.StepDefinitionMatch.new(..))  && args(arguments, stepDefinition, featurePath, step, localizedXStreams,..)")
    public void argumentOffset(List<Argument> arguments, StepDefinition stepDefinition, String featurePath, PickleStep step, LocalizedXStreams localizedXStreams) {
    }

    @Around("argumentOffset(arguments, stepDefinition, featurePath, step, localizedXStreams)")
    public Object argumentOffset(ProceedingJoinPoint joinPoint, List<Argument> arguments, StepDefinition stepDefinition, String featurePath, PickleStep step, LocalizedXStreams localizedXStreams) throws Throwable {
        if (step.getClass().equals(PickleStepCustom.class)) {
            List<Argument> shiftedArguments = new ArrayList<>();
            boolean isCritical = ((PickleStepCustom) step).isCritical();

            for (Argument argument : arguments) {
                Argument arg = null;
                if (argument.getClass().equals(ArgumentCustom.class) && ((ArgumentCustom) argument).isModified()) {
                    if (isCritical) {
                        arg = new ArgumentCustom(argument.getOffset() - NON_CRITICAL.length(), argument.getVal(), false);
                    }
                } else {
                    if (!isCritical) {
                        arg = new ArgumentCustom(argument.getOffset() + NON_CRITICAL.length(), argument.getVal(), true);
                    }
                }
                shiftedArguments.add(Optional.ofNullable(arg).orElse(argument));
            }
            return joinPoint.proceed(new Object[]{shiftedArguments, stepDefinition, featurePath, step, localizedXStreams});
        }
        return joinPoint.proceed(new Object[]{arguments, stepDefinition, featurePath, step, localizedXStreams});
    }
}
