//package ru.sbtqa.tag.pagefactory.aspects;
//
//import cucumber.runtime.model.CucumberFeature;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Pointcut;
//import ru.sbtqa.tag.pagefactory.fragments.FragmentReplacer;
//import ru.sbtqa.tag.pagefactory.properties.Configuration;
//import ru.sbtqa.tag.pagefactory.utils.GherkinSerializer;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
////@Aspect
//public class FragmentsAspect {
//
//    private static final Configuration PROPERTIES = Configuration.create();
//
//    @Pointcut("execution(* cucumber.runtime.FeaturePathFeatureSupplier.get(..))")
//    public void cucumberFeatures() {
//    }
//
//    @Around("cucumberFeatures()")
//    public Object replaceSteps(ProceedingJoinPoint joinPoint) throws Throwable {
//        List<CucumberFeature> features = (List<CucumberFeature>) joinPoint.proceed();
//        // filter out empty files
//        features = features.stream()
//                .filter(cucumberFeature -> cucumberFeature.getGherkinFeature().getFeature() != null)
//                .collect(Collectors.toList());
//
//        if (PROPERTIES.isFragmentsEnabled()) {
//            FragmentReplacer fragmentReplacer = new FragmentReplacer(features);
//            fragmentReplacer.replace();
//            features = new GherkinSerializer().reserializeFeatures(features);
//        }
//
//        return features;
//    }
//}
