package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.runtime.model.CucumberFeature;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import ru.sbtqa.tag.pagefactory.data.DataFactory;
import ru.sbtqa.tag.pagefactory.data.DataParser;
import ru.sbtqa.tag.pagefactory.fragments.FragmentReplacer;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

@Aspect
public class CucumberAspect {

    private static final Configuration PROPERTIES = Configuration.create();

    @Around("call(* cucumber.api.junit.Cucumber.addChildren(..))")
    public void replaceSteps(ProceedingJoinPoint joinPoint) throws Throwable {

        List<CucumberFeature> cucumberFeatures = (List<CucumberFeature>) joinPoint.getArgs()[0];

        if (PROPERTIES.isFragmentsEnabled()) {
            FragmentReplacer fragmentReplacer = new FragmentReplacer(cucumberFeatures);
            fragmentReplacer.replace();
        }

        if (DataFactory.getDataProvider() != null) {
            DataParser dataParser = new DataParser();
            dataParser.replaceDataPlaceholders(cucumberFeatures);
        }
        joinPoint.proceed();
    }
}
