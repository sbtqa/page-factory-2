package ru.sbtqa.tag.pagefactory.aspects;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import java.lang.reflect.Field;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;
import static java.util.UUID.randomUUID;

@Aspect
public class CriticalStepCheckAspect {

    private static final String FIELD_NAME = "step";
    private static final Logger LOG = LoggerFactory.getLogger(CriticalStepCheckAspect.class);

    @Pointcut("execution(* cucumber.runtime.StepDefinitionMatch.runStep(..))")
    public void runStep() {
    }

    @Around("runStep()")
    public Object runStep(ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint.getThis() != null) {
            Object instance = joinPoint.getThis();
            Field step = instance.getClass().getDeclaredField(FIELD_NAME);
            step.setAccessible(true);
            PickleStepCustom pickle = (PickleStepCustom) step.get(instance);

            if (pickle.isCritical()) {
                return joinPoint.proceed();
            } else {
                try {
                    return joinPoint.proceed();
                } catch (AssertionError e) {
                    pickle.setIsFailed(true);
                    step.set(instance, pickle);
                    LOG.warn("Uncritical step failed", e);

                    AllureLifecycle lifecycle = Allure.getLifecycle();

                    lifecycle.updateStep(stepResult ->
                            stepResult.setStatus(Status.BROKEN));
                    String uuid = randomUUID().toString();
                    lifecycle.startStep(uuid, new StepResult().withStatus(Status.BROKEN).withName(e.toString()));
                    lifecycle.stopStep(uuid);
                    lifecycle.stopStep();

                    return null;
                }
            }
        } else {
            return joinPoint.proceed();
        }
    }
}
