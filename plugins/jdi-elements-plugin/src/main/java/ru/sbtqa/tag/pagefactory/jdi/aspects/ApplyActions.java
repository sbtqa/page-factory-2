package ru.sbtqa.tag.pagefactory.jdi.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.jdi.actions.JdiPageActions;

@Aspect
public class ApplyActions {

    private static PageActions pageActions = new JdiPageActions();

    @Before("preinitialization(ru.sbtqa.tag.pagefactory.JDIPage.new(..))")
    public void apply() {
        Environment.setPageActions(pageActions);
    }
}
