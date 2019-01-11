package ru.sbtqa.tag.pagefactory.aspects.report;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.allurehelper.ParamsHelper;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

@Aspect
public class SetCheckbox {

    private static final Configuration PROPERTIES = Configuration.create();

    @Pointcut("execution(* ru.sbtqa.tag.pagefactory.*.setCheckbox(..)) && if()")
    public static boolean isSetCheckboxReportEnabled() {
        return PROPERTIES.isSetCheckboxReportEnabled();
    }

    @After("isSetCheckboxReportEnabled()")
    public void reportClick(JoinPoint joinPoint) {
        String elementTitle = (String) joinPoint.getArgs()[0];

        ParamsHelper.addParam("'%s is checked'", new String[]{elementTitle});
    }
}
