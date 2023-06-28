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
//public class SetCheckbox {
//
//    private static final Configuration PROPERTIES = Configuration.create();
//
//    @Pointcut("execution(* ru.sbtqa.tag.stepdefs.*.*.setCheckBox(..)) && args(elementTitle,..) && if()")
//    public static boolean isSetCheckboxReportEnabled(String elementTitle) {
//        return PROPERTIES.isSetCheckboxReportEnabled();
//    }
//
//    @After("isSetCheckboxReportEnabled(elementTitle)")
//    public void reportClick(JoinPoint joinPoint, String elementTitle) {
//        ParamsHelper.addParam(elementTitle, "true");
//    }
//}
