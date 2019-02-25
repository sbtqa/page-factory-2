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

    @Pointcut("execution(* ru.sbtqa.tag.pagefactory.*.pressKey(..)) && if()")
    public static boolean isPressKeyReportEnabled() {
        return PROPERTIES.isPressKeyReportEnabled();
    }

    @After("isPressKeyReportEnabled()")
    public void reportClick(JoinPoint joinPoint) {
        String keyName = (String) joinPoint.getArgs()[0];

        ParamsHelper.addParam("\"%s\" is pressed", new String[]{keyName});
    }
}
