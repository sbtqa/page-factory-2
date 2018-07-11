package ru.sbtqa.tag.pagefactory;

import cucumber.api.event.TestRunFinished;
import cucumber.api.formatter.Formatter;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.Resource;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.junit.Assertions;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitOptions;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.FeatureBuilder;
import gherkin.events.PickleEvent;
import gherkin.pickles.Pickle;
import gherkin.pickles.PickleStep;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class Runner extends ParentRunner<FeatureRunner> {

    private final JUnitReporter jUnitReporter;
    private final List<FeatureRunner> children = new ArrayList<>();
    private final Runtime runtime;

    /**
     * Constructor called by JUnit.
     *
     * @param clazz the class with the @RunWith annotation.
     * @throws java.io.IOException                         if there is a problem
     * @throws org.junit.runners.model.InitializationError if there is another problem
     */
    public Runner(Class clazz) throws InitializationError, IOException {
        super(clazz);
        ClassLoader classLoader = clazz.getClassLoader();
        Assertions.assertNoCucumberAnnotatedMethods(clazz);

        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        runtime = createRuntime(resourceLoader, classLoader, runtimeOptions);
        final JUnitOptions junitOptions = new JUnitOptions(runtimeOptions.getJunitOptions());



//        final List<CucumberFeature> cucumberFeatures = new ArrayList<CucumberFeature>();
//        final FeatureBuilder builder = new FeatureBuilder(cucumberFeatures);
//        Iterable<Resource> resources = resourceLoader.resources("src/test/resources/features", ".feature");



        final List<CucumberFeature> cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader, runtime.getEventBus());

        List<String> featurePaths = new ArrayList<>();
        featurePaths.add("src/test/resources/fragmentscenario");
        List<CucumberFeature> features = CucumberFeature.load(resourceLoader, featurePaths);
        System.out.println(features);


        jUnitReporter = new JUnitReporter(runtime.getEventBus(), runtimeOptions.isStrict(), junitOptions);
        addChildren(cucumberFeatures);
    }

    /**
     * Create the Runtime. Can be overridden to customize the runtime or backend.
     *
     * @param resourceLoader used to load resources
     * @param classLoader    used to load classes
     * @param runtimeOptions configuration
     * @return a new runtime
     * @throws InitializationError if a JUnit error occurred
     * @throws IOException         if a class or resource could not be loaded
     * @deprecated Neither the runtime nor the backend or any of the classes involved in their construction are part of
     * the public API. As such they should not be  exposed. The recommended way to observe the cucumber process is to
     * listen to events by using a plugin. For example the JSONFormatter.
     */
    @Deprecated
    protected Runtime createRuntime(ResourceLoader resourceLoader, ClassLoader classLoader,
                                    RuntimeOptions runtimeOptions) throws InitializationError, IOException {
        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        return new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
    }

    @Override
    public List<FeatureRunner> getChildren() {
        return children;
    }

    @Override
    protected Description describeChild(FeatureRunner child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(FeatureRunner child, RunNotifier notifier) {
        child.run(notifier);
    }

    @Override
    protected Statement childrenInvoker(RunNotifier notifier) {
        final Statement features = super.childrenInvoker(notifier);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                features.evaluate();
                runtime.getEventBus().send(new TestRunFinished(runtime.getEventBus().getTime()));
            }
        };
    }

    private void addChildren(List<CucumberFeature> cucumberFeatures) throws InitializationError {

        try {
//            URI firstURI = new URI("src/test/resources/fragments");
            Files.walk(Paths.get("src/test/resources/fragments"))
                    .filter(Files::isRegularFile)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            FeatureRunner featureRunner = new FeatureRunner(cucumberFeature, runtime, jUnitReporter);

            List<Object> children1 = null;
            try {
                children1 = (List<Object>) FieldUtils.readField(featureRunner, "children", true);

                for (Object pickleRunner : children1) {
                    Object pe = FieldUtils.readField(pickleRunner, "pickleEvent", true);
                    Object p = FieldUtils.readField(pe, "pickle", true);
                    List<PickleStep> ps = (List<PickleStep>) FieldUtils.readField(p, "steps", true);


                    List<PickleStep> steps = new ArrayList<>();
                    steps.addAll(ps);
                    List<PickleStep> fragment = new ArrayList<>();
                    fragment.add(new PickleStep("user (checks that the field is empty) \"first name\"", steps.get(0).getArgument(), steps.get(0).getLocations()));
                    fragment.add(new PickleStep("user (fill the field) \"first name\" \"Alex\"", steps.get(0).getArgument(), steps.get(0).getLocations()));
//                    fragment.add(new PickleStep("user (checks that the field is not empty) \"first name\"", new ArrayList<>(), new ArrayList<>()));
//                    fragment.add(new PickleStep("user (checks value) \"first name\" \"Alex\"", new ArrayList<>(), new ArrayList<>()));
//                    fragment.add(new PickleStep("user (check that values are not equal) \"first name\" \"Billy\"", new ArrayList<>(), new ArrayList<>()));

                    int fragmentIndex = getFragmentIndex(steps, "user (insert fragment) \"identification\"");
                    if (fragmentIndex > -1) {
                       steps.remove(fragmentIndex);
                       steps.addAll(fragmentIndex, fragment);
                    }

                    FieldUtils.writeField(p, "steps", steps, true);

//                    for (Object s : ps) {
//                        Object t = FieldUtils.readField(s, "text", true);
//                        if (t.equals("user (insert fragment) \"identification\"")) {
////                            FieldUtils.writeField(s, "text", "user (click the button) \"Contact\"", true);
//                        }
////                    System.out.println(t);
//
//                    }
                    System.out.println(1);
                }
//






                System.out.println("stop");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (!featureRunner.isEmpty()) {
                children.add(featureRunner);
            }
        }
    }

    private static int getFragmentIndex(List<PickleStep> steps, String fragmentName) {
        for (PickleStep step : steps) {
            if (fragmentName.equals(step.getText())) {
                return steps.indexOf(step);
            }
        }

        return -1;
    }
}
