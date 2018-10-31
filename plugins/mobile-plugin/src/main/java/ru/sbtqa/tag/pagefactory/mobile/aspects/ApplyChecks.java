package ru.sbtqa.tag.pagefactory.mobile.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import ru.sbtqa.tag.pagefactory.checks.PageChecks;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.checks.MobilePageChecks;

@Aspect
public class ApplyChecks {

    private static PageChecks pageChecks = new MobilePageChecks();

    @Before("execution(ru.sbtqa.tag.pagefactory.mobile.MobilePage.new(..))")
    public void apply() {
        Environment.setPageChecks(pageChecks);
    }
}
