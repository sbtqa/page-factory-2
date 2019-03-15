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

    @Pointcut("execution(* ru.sbtqa.tag.stepdefs.*.*.click(..)) && args(elementTitle,..) && if()")
    public static boolean isClickReportEnabled(String elementTitle) {
        return PROPERTIES.isClickReportEnabled();
    }

    @After("isClickReportEnabled(elementTitle)")
    public void reportClick(JoinPoint joinPoint, String elementTitle) {
        ParamsHelper.addParam("Button pressed", elementTitle);
    }
}
