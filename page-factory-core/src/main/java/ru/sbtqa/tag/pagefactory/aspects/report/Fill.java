//package ru.sbtqa.tag.pagefactory.aspects.report;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import ru.sbtqa.tag.pagefactory.allure.ParamsHelper;
//import ru.sbtqa.tag.pagefactory.properties.Configuration;
//
//@Aspect
//public class Fill {
//
//    private static final Configuration PROPERTIES = Configuration.create();
//
//    @Pointcut("execution(* ru.sbtqa.tag.stepdefs.*.*.fill(..)) && args(elementTitle, text,..) && if()")
//    public static boolean isFillReportEnabled(String elementTitle, String text) {
//        return PROPERTIES.isFillReportEnabled();
//    }
//
//    @After("isFillReportEnabled(elementTitle, text)")
//    public void reportFill(JoinPoint joinPoint, String elementTitle, String text) {
//        ParamsHelper.addParam(elementTitle, text);
//    }
//}
