//package ru.sbtqa.tag.pagefactory.aspects.report;
//
//import cucumber.api.Result;
//import cucumber.api.event.Event;
//import cucumber.api.event.TestStepFinished;
//import io.qameta.allure.Allure;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.openqa.selenium.InvalidElementStateException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import ru.sbtqa.tag.pagefactory.allure.ErrorHandler;
//import ru.sbtqa.tag.pagefactory.allure.ParamsHelper;
//import ru.sbtqa.tag.pagefactory.allure.Type;
//import ru.sbtqa.tag.pagefactory.environment.Environment;
//import ru.sbtqa.tag.pagefactory.properties.Configuration;
//
//@Aspect
//public class ErrorHandlerAspect {
//
//    private static final Logger LOG = LoggerFactory.getLogger(ErrorHandlerAspect.class);
//    private static final Configuration PROPERTIES = Configuration.create();
//
//    private final ThreadLocal<String> stepText = ThreadLocal.withInitial(() -> "");
//
//    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
//    public static boolean sendStepFinished(Event event) {
//        return event instanceof TestStepFinished;
//    }
//
//    @Around("sendStepFinished(event)")
//    public void sendStepFinished(ProceedingJoinPoint joinPoint, Event event) throws Throwable {
//        TestStepFinished testStepFinished = (TestStepFinished) event;
//
//        if (testStepFinished.result.getStatus() == Result.Type.FAILED
//                && !Environment.isDriverEmpty()
//                && !stepText.get().equals(Allure.getLifecycle().getCurrentTestCaseOrStep().toString())) {
//            stepText.set(Allure.getLifecycle().getCurrentTestCaseOrStep().toString());
//            ErrorHandler.attachError(testStepFinished.result.getError());
//            ErrorHandler.attachScreenshot();
//            if (PROPERTIES.isReportXmlAttachEnabled()) {
//                try {
//                    ParamsHelper.addAttachmentToRender(Environment.getDriverService().getDriver().getPageSource().getBytes(), "Page source", Type.XML);
//                } catch (InvalidElementStateException e) {
//                    LOG.error("Can't attach page source", e);
//                }
//            }
//        }
//        joinPoint.proceed();
//    }
//}