package ru.sbtqa.tag.pagefactory.mobile;

import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.find.FindUtils;
import ru.sbtqa.tag.pagefactory.mobile.actions.MobilePageActions;
import ru.sbtqa.tag.pagefactory.mobile.checks.MobilePageChecks;
import ru.sbtqa.tag.pagefactory.mobile.junit.MobileSetupSteps;

/**
 * Inherit your mobile page objects from this class
 */
public abstract class MobilePage implements Page {

    public MobilePage() {
        MobileSetupSteps.initMobile();
        Environment.setPageActions(new MobilePageActions());
        Environment.setPageChecks(new MobilePageChecks());
        Environment.setFindUtils(new FindUtils());
    }
}