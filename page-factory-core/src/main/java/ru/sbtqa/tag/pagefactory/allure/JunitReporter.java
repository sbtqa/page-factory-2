package ru.sbtqa.tag.pagefactory.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import java.util.Arrays;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import ru.sbtqa.tag.qautils.i18n.I18N;

public class JunitReporter {

    public static Object handleStep(ProceedingJoinPoint joinPoint) throws Throwable {

        // FIXME: need to get another way to filter junit only steps cuz getStackTrace is very hard
        boolean isFromCucumber = Arrays.stream(
                Thread.currentThread().getStackTrace()
        ).anyMatch(stackTraceElement -> stackTraceElement.getClassName().matches("ru\\.sbtqa\\.tag\\.stepdefs\\.[en.|ru.].*"));
        if (isFromCucumber) {
            return joinPoint.proceed();
        } else {
                Object[] args = joinPoint.getArgs();
                String stepUid = joinPoint.getSignature().toLongString() + Arrays.toString(args) + System.currentTimeMillis();
                String methodName = joinPoint.getSignature().getName();
                String methodNameWithArgsCount = methodName + "." + args.length;

                String stepNameI18n = I18N.getI18n(joinPoint.getSignature().getDeclaringType()).get(methodName + "." + args.length);

                String stepName = stepNameI18n.equals(methodNameWithArgsCount) ? methodName : stepNameI18n;


                stepName = String.format(stepName, args);



                Allure.getLifecycle().startStep(stepUid, new StepResult().setName(stepName));
            System.out.println(stepName);


            try {
                Object r = joinPoint.proceed();
                Allure.getLifecycle().updateStep(stepUid, stepResult -> stepResult.setStatus(Status.PASSED));
                if (stepName.equals(methodName)) {
                    addParamAsSubstep(joinPoint);
                }
                return r;
            } catch (Throwable t) {
                Allure.getLifecycle().updateStep(stepUid, stepResult ->
                        stepResult.setStatus(Status.FAILED).setStatusDetails(new StatusDetails().setTrace(ExceptionUtils.getStackTrace(t)).setMessage(t.getMessage())));
                if (stepName.equals(methodName)) {
                    addParamAsSubstep(joinPoint);
                }
                throw t;
            } finally {
                System.out.println("STOP!");
                Allure.getLifecycle().stopStep();


            }
        }
    }

    private static void addParamAsSubstep(ProceedingJoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            System.out.println(arg);
            Allure.getLifecycle().startStep(joinPoint.getSignature().toLongString() + System.currentTimeMillis(), new StepResult().setName(arg.toString()).setStatus(Status.PASSED));
            Allure.getLifecycle().stopStep();
        }
    }

}
