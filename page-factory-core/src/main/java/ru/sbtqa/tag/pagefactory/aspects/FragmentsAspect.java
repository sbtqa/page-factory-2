package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.event.TestRunStarted;
import cucumber.runner.EventBus;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.FeatureLoader;
import io.cucumber.core.model.FeaturePath;
import java.util.Collections;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.fragments.FragmentReplacer;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.utils.GherkinSerializer;

import java.util.List;
import java.util.stream.Collectors;

@Aspect
public class FragmentsAspect {

    private static final Configuration PROPERTIES = Configuration.create();

    @Pointcut("execution(* cucumber.runtime.FeaturePathFeatureSupplier.get(..))")
    public void cucumberFeatures() {
    }

    @Around("cucumberFeatures()")
    public Object replaceSteps(ProceedingJoinPoint joinPoint) throws Throwable {
//            // FIXME
////            runtimeOptions.getPlugins(); // to create the formatter objects
////            bus.send(new TestRunStarted(bus.getTime()));
//            for (CucumberFeature feature : cucumberFeatures) {
//                feature.sendTestSourceRead(bus);
//            }

        List<CucumberFeature> features = (List<CucumberFeature>) joinPoint.proceed();
        // filter out empty files
        features = features.stream()
                .filter(cucumberFeature -> cucumberFeature.getGherkinFeature().getFeature() != null)
                .collect(Collectors.toList());

        if (PROPERTIES.isFragmentsEnabled()) {
            FragmentReplacer fragmentReplacer = new FragmentReplacer(features);
            fragmentReplacer.replace();
            features = new GherkinSerializer().reserializeFeatures(features);
        }

        return features;
    }
}
