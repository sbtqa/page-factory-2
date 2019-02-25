package ru.sbtqa.tag.pagefactory.aspects.report;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.allure.ParamsHelper;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

@Aspect
public class Click {

    private static final Configuration PROPERTIES = Configuration.create();

    @Pointcut("execution(* ru.sbtqa.tag.pagefactory.*.click(..)) && if()")
    public static boolean isClickReportEnabled() {
        return PROPERTIES.isClickReportEnabled();
    }

    @After("isClickReportEnabled()")
    public void reportClick(JoinPoint joinPoint) {
        String elementTitle = (String) joinPoint.getArgs()[0];

        ParamsHelper.addParam("\"%s\" is clicked", new String[]{elementTitle});
    }
}
