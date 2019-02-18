package ru.sbtqa.tag.pagefactory.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

import static java.util.UUID.randomUUID;

public class AllureNonCriticalFailure {

    private AllureNonCriticalFailure() {
    }

    /**
     * Add thread as key and exception as value to failure map, for non
     * CriticalError during test executing
     *
     * @param throvv - throw stack trace
     */
    public static void fire(Throwable throvv) {
        Allure.getLifecycle().startStep(randomUUID().toString(), new StepResult()
                .withName(throvv.getMessage())
                .withStatus(Status.FAILED));
        Allure.getLifecycle().stopStep();
    }
}
