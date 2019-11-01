package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.runtime.StepDefinition;
import gherkin.pickles.PickleStep;
import io.cucumber.stepexpression.Argument;
import io.cucumber.stepexpression.ExpressionArgument;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.optional.PickleStepTag;

import static ru.sbtqa.tag.pagefactory.optional.PickleStepTag.NON_CRITICAL;

@Aspect
public class CriticalStepAspect {

    @Pointcut(value = "execution(* cucumber.runner.Glue.stepDefinitionMatch(..)) && args(featurePath, step,..)")
    public void removeNonCriticalSign(String featurePath, PickleStep step) {
    }

    @Pointcut(value = "call(cucumber.runner.PickleStepDefinitionMatch.new(..)) && args(arguments, stepDefinition, featurePath, step,..)")
    public void argumentOffset(List<Argument> arguments, StepDefinition stepDefinition,
                               String featurePath, PickleStep step) {
    }

    @Pointcut(value = "execution(* cucumber.runtime.snippets.SnippetGenerator.getSnippet(..)) && args(step,..) && if()")
    public static boolean getSnippet(PickleStep step) {
        return step.getText().startsWith(NON_CRITICAL);
    }

    @Around(value = "removeNonCriticalSign(featurePath, step)")
    public Object removeNonCriticalSign(ProceedingJoinPoint joinPoint, String featurePath, PickleStep step) throws Throwable {
        PickleStepTag pickleStepTag = step instanceof PickleStepTag ? (PickleStepTag) step : new PickleStepTag(step);
        pickleStepTag.removeNonCriticalSign();
        return joinPoint.proceed(new Object[]{featurePath, pickleStepTag});
    }

    @Around(value = "argumentOffset(arguments, stepDefinition, featurePath, step)",
            argNames = "joinPoint,arguments,stepDefinition,featurePath,step")
    public Object argumentOffset(ProceedingJoinPoint joinPoint, List<Argument> arguments, StepDefinition stepDefinition,
                                 String featurePath, PickleStep step) throws Throwable {
        for (Argument argument : arguments) {
            if (argument instanceof ExpressionArgument && hasReplaceableArgument(step, argument)) {
                int start = ((ExpressionArgument) argument).getGroup().getStart();
                int end = ((ExpressionArgument) argument).getGroup().getEnd();

                int offset = NON_CRITICAL.length();
                FieldUtils.writeField(FieldUtils.readField(FieldUtils.readField(argument, "argument", true), "group", true), "start", start + offset, true);
                FieldUtils.writeField(FieldUtils.readField(FieldUtils.readField(argument, "argument", true), "group", true), "end", end + offset, true);
            }
        }
        return joinPoint.proceed();
    }

    private boolean hasReplaceableArgument(PickleStep step, Argument argument) {
        return argument.getValue() != null
                && step instanceof PickleStepTag && ((PickleStepTag) step).isNonCritical();
    }

    @Around("getSnippet(step)")
    public Object getSnippet(ProceedingJoinPoint joinPoint, PickleStep step) throws Throwable {
        String stepText = step.getText();
        List<String> joinPointResults = (List<String>) joinPoint.proceed();

        for (String joinPointResult : joinPointResults) {
            if (stepText.contains("\"")) {
                String replaceableTextRegExp = "\\(\"(\\s)*\\?(\\s)*";
                joinPointResults.set(joinPointResults.indexOf(joinPointResult), joinPointResult.replaceFirst(replaceableTextRegExp, "(\""));
            }
        }

        return joinPointResults;
    }
}
