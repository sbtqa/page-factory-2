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
//public class Select {
//
//    private static final Configuration PROPERTIES = Configuration.create();
//
//    @Pointcut("execution(* ru.sbtqa.tag.stepdefs.*.*.select(..)) && args(elementTitle, option,..) && if()")
//    public static boolean isSelectReportEnabled(String elementTitle, String option) {
//        return PROPERTIES.isSelectReportEnabled();
//    }
//
//    @After("isSelectReportEnabled(elementTitle, option)")
//    public void reportClick(JoinPoint joinPoint, String elementTitle, String option) {
//        ParamsHelper.addParam(elementTitle, option);
//    }
//}
