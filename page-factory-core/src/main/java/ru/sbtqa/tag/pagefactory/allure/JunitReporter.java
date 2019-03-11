package ru.sbtqa.tag.pagefactory.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import ru.sbtqa.tag.qautils.i18n.I18N;

import java.util.Arrays;

public class JunitReporter {

    public static Object handleStep(ProceedingJoinPoint joinPoint) throws Throwable {

        // FIXME: need to get another way to filter junit only steps cuz getStackTrace is very hard
        boolean isFromCucumber = Arrays.stream(
                Thread.currentThread().getStackTrace()
        ).anyMatch(stackTraceElement -> stackTraceElement.getClassName().matches("ru\\.sbtqa\\.tag\\.stepdefs\\.[en.|ru.].*"));
        if (isFromCucumber) {
            return joinPoint.proceed();
        } else {
                String stepUid = joinPoint.getSignature().toLongString() + System.currentTimeMillis();
                String methodName = joinPoint.getSignature().getName();
//                System.out.println(methodName);
//                try {
//                    System.out.println((String) joinPoint.getArgs()[0]);
//                } catch (Exception e) {}
//
//                System.out.println();

                String stepName = I18N.getI18n(joinPoint.getSignature().getDeclaringType()).get(methodName);

                Allure.getLifecycle().startStep(stepUid, new StepResult().setName(stepName));
            try {
                Object r = joinPoint.proceed();
                Allure.getLifecycle().updateStep(stepUid, stepResult -> stepResult.setStatus(Status.PASSED));
                return r;
            } catch (Throwable t) {
                Allure.getLifecycle().updateStep(stepUid, stepResult ->
                        stepResult.setStatus(Status.FAILED).setStatusDetails(new StatusDetails().setTrace(ExceptionUtils.getStackTrace(t)).setMessage(t.getMessage())));
                throw t;
            } finally {
                Allure.getLifecycle().stopStep();
            }
        }
    }
}
