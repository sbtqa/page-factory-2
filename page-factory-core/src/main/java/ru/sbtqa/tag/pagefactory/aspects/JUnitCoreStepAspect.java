//package ru.sbtqa.tag.pagefactory.aspects;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import ru.sbtqa.tag.pagefactory.allure.JunitReporter;
//
//@Aspect
//public class JUnitCoreStepAspect {
//
//    @Around("execution(public * ru.sbtqa.tag.pagefactory.junit.CoreSteps.*(..))")
//    public Object handleStep(ProceedingJoinPoint joinPoint) throws Throwable {
//        return JunitReporter.handleStep(joinPoint);
//    }
//
//}
