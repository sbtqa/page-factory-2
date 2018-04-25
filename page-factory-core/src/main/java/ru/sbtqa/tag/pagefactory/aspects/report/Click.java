package ru.sbtqa.tag.pagefactory.aspects.report;

import org.aeonbits.owner.ConfigFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.allurehelper.ParamsHelper;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

@Aspect
public class Click {

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

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
