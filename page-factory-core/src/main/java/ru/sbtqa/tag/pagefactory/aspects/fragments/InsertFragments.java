package ru.sbtqa.tag.pagefactory.aspects.fragments;

import cucumber.runtime.model.CucumberFeature;
import java.util.List;
import org.aeonbits.owner.ConfigFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.utils.FragmentsUtils;

@Aspect
public class InsertFragments {

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    @Pointcut("call(* *.addChildren(..)) && if()")
    public static boolean addChildren() {
        return PROPERTIES.isFragmentsEnabled();
    }

    @Around("addChildren()")
    public void replaceFragments(ProceedingJoinPoint joinPoint) throws Throwable {
        FragmentsUtils.replace((List<CucumberFeature>) joinPoint.getArgs()[0]);
        joinPoint.proceed();
    }
}
