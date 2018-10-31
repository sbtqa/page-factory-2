package ru.sbtqa.tag.pagefactory.web.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import ru.sbtqa.tag.pagefactory.checks.PageChecks;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.web.checks.WebPageChecks;

@Aspect
public class ApplyChecks {

    private static PageChecks pageChecks = new WebPageChecks();

    @Before("execution(ru.sbtqa.tag.pagefactory.WebPage.new(..))")
    public void apply() {
        Environment.setPageChecks(pageChecks);
    }
}
