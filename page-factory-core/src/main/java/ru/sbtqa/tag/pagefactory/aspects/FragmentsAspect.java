package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.event.TestRunStarted;
import cucumber.runner.EventBus;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.fragments.FragmentReplacer;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.utils.GherkinSerializer;

import java.util.List;
import java.util.stream.Collectors;

import static cucumber.runtime.model.CucumberFeature.load;

@Aspect
public class FragmentsAspect {

    private static final Configuration PROPERTIES = Configuration.create();

    @Pointcut("execution(* cucumber.runtime.RuntimeOptions.cucumberFeatures(..)) && args(resourceLoader, bus)")
    public void cucumberFeatures(ResourceLoader resourceLoader, EventBus bus) {
    }

    @Around("cucumberFeatures(resourceLoader, bus)")
    public Object replaceSteps(ProceedingJoinPoint joinPoint, ResourceLoader resourceLoader, EventBus bus) throws Throwable {
        if (PROPERTIES.isFragmentsEnabled()) {
            RuntimeOptions runtimeOptions = (RuntimeOptions) joinPoint.getTarget();
            List<CucumberFeature> cucumberFeatures = load(resourceLoader, runtimeOptions.getFeaturePaths(), System.out);

            // filter out empty files
            cucumberFeatures = cucumberFeatures.stream()
                    .filter(cucumberFeature -> cucumberFeature.getGherkinFeature().getFeature() != null)
                    .collect(Collectors.toList());

            FragmentReplacer fragmentReplacer = new FragmentReplacer(cucumberFeatures);
            fragmentReplacer.replace();

            // reserealize and align all features
            cucumberFeatures = new GherkinSerializer().getSource(cucumberFeatures);

            runtimeOptions.getPlugins(); // to create the formatter objects
            bus.send(new TestRunStarted(bus.getTime()));
            for (CucumberFeature feature : cucumberFeatures) {
                feature.sendTestSourceRead(bus);
            }
            return cucumberFeatures;
        } else {
            return joinPoint.proceed();
        }
    }

}
