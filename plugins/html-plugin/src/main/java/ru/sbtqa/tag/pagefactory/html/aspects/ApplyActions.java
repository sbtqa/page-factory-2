package ru.sbtqa.tag.pagefactory.html.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.html.actions.HtmlPageActions;

@Aspect
public class ApplyActions {

    private static final PageActions pageActions = new HtmlPageActions();

    @Before("execution(ru.sbtqa.tag.pagefactory.HTMLPage.new(..))")
    public void apply() {
        Environment.setPageActions(pageActions);
    }
}
