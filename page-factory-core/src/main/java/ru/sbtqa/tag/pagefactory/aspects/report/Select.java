package ru.sbtqa.tag.pagefactory.aspects.report;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.allure.ParamsHelper;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

@Aspect
public class Select {

    private static final Configuration PROPERTIES = Configuration.create();

    @Pointcut("execution(* ru.sbtqa.tag.pagefactory.*.select(..)) && if()")
    public static boolean isSelectReportEnabled() {
        return PROPERTIES.isSelectReportEnabled();
    }

    @After("isSelectReportEnabled()")
    public void reportClick(JoinPoint joinPoint) {
        String elementTitle = (String) joinPoint.getArgs()[0];
        String option = (String) joinPoint.getArgs()[1];

        ParamsHelper.addParam("In the select \"%s\" is selected option \"%s\"", new String[]{elementTitle, option});
    }
}
