package ru.sbtqa.tag.pagefactory.aspects.report;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.allurehelper.ParamsHelper;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

@Aspect
public class Fill {

    private static final Configuration PROPERTIES = Configuration.create();

    @Pointcut("execution(* ru.sbtqa.tag.pagefactory.*.fill(..)) && if()")
    public static boolean isFillReportEnabled() {
        return PROPERTIES.isFillReportEnabled();
    }

    @After("isFillReportEnabled()")
    public void reportFill(JoinPoint joinPoint) {
        String elementTitle = (String) joinPoint.getArgs()[0];
        String text = (String) joinPoint.getArgs()[1];

        ParamsHelper.addParam("\"%s\" is filled with text \"%s\"", new String[]{elementTitle, text});
    }
}
