package ru.sbtqa.tag.pagefactory.aspects.report;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.allure.ParamsHelper;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

@Aspect
public class PressKey {

    private static final Configuration PROPERTIES = Configuration.create();

    @Pointcut("execution(* ru.sbtqa.tag.stepdefs.*.*.pressKey(..)) && args(keyName,..) && if()")
    public static boolean isPressKeyReportEnabled(String keyName) {
        return PROPERTIES.isPressKeyReportEnabled();
    }

    @After("isPressKeyReportEnabled(keyName)")
    public void reportClick(JoinPoint joinPoint, String keyName) {
        ParamsHelper.addParam("Key pressed", keyName);
    }
}
