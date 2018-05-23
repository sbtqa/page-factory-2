package ru.sbtqa.tag.pagefactory.mobile.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.actions.MobilePageActions;

@Aspect
public class ApplyActions {

    private static PageActions pageActions = new MobilePageActions();

    @Before("preinitialization(ru.sbtqa.tag.pagefactory.mobile.MobilePage.new(..))")
    public void apply() {
        Environment.setPageActions(pageActions);
    }
}
