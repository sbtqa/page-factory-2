package ru.sbtqa.tag.pagefactory.aspects.fragments;

import cucumber.runtime.model.CucumberFeature;
import java.util.List;
import org.aeonbits.owner.ConfigFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.fragments.FragmentReplacer;

@Aspect
public class InsertFragments {

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    @Pointcut("call(* cucumber.api.junit.Cucumber.addChildren(..)) && if()")
    public static boolean addChildren() {
        return PROPERTIES.isFragmentsEnabled();
    }

    @Around("addChildren()")
    public void replaceFragments(ProceedingJoinPoint joinPoint) throws Throwable {
        FragmentReplacer fragmentReplacer = new FragmentReplacer((List<CucumberFeature>) joinPoint.getArgs()[0]);
        fragmentReplacer.replace();

        joinPoint.proceed();
    }
}
