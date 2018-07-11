package ru.sbtqa.tag.pagefactory;

import cucumber.api.event.TestRunFinished;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.junit.Assertions;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitOptions;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Step;
import gherkin.pickles.PickleStep;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class Runner extends ParentRunner<FeatureRunner> {

    // TODO взять из i18n
    public static final String INSERT_FRAGMENT = "^(?:user |he |)\\(insert fragment\\) \"([^\"]*)\"$";
    private final JUnitReporter jUnitReporter;
    private final List<FeatureRunner> children = new ArrayList<>();
    private final Runtime runtime;

    /**
     * Constructor called by JUnit.
     *
     * @param clazz the class with the @RunWith annotation.
     * @throws java.io.IOException if there is a problem
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

        final List<CucumberFeature> cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader, runtime.getEventBus());

        jUnitReporter = new JUnitReporter(runtime.getEventBus(), runtimeOptions.isStrict(), junitOptions);
        addChildren(cucumberFeatures);
    }

    /**
     * Create the Runtime. Can be overridden to customize the runtime or backend.
     *
     * @param resourceLoader used to load resources
     * @param classLoader used to load classes
     * @param runtimeOptions configuration
     * @return a new runtime
     * @throws InitializationError if a JUnit error occurred
     * @throws IOException if a class or resource could not be loaded
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
        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            FeatureRunner featureRunner = new FeatureRunner(cucumberFeature, runtime, jUnitReporter);

            try {
                List<Object> children = (List<Object>) FieldUtils.readField(featureRunner, "children", true);

                for (Object pickleRunner : children) {
                    Object pe = FieldUtils.readField(pickleRunner, "pickleEvent", true);
                    Object p = FieldUtils.readField(pe, "pickle", true);
                    List<PickleStep> ps = (List<PickleStep>) FieldUtils.readField(p, "steps", true);

                    FieldUtils.writeField(p, "steps", replaceFragments(ps), true);
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (!featureRunner.isEmpty()) {
                children.add(featureRunner);
            }
        }
    }

    private List<PickleStep> replaceFragments(List<PickleStep> steps) {
        List<PickleStep> fragmentedSteps = new ArrayList<>();
        Pattern pattern = Pattern.compile(INSERT_FRAGMENT);

        for (PickleStep step : steps) {
            Matcher matcher = pattern.matcher(step.getText());
            if (matcher.find()) {
                String fragmentName = matcher.group(1);

                ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                ResourceLoader resourceLoader = new MultiLoader(classLoader);
                List<String> featurePaths = new ArrayList<>();
                featurePaths.add("src/test/resources/fragmentscenario");
                List<CucumberFeature> features = CucumberFeature.load(resourceLoader, featurePaths);

                for (Step stp : getSteps(features, fragmentName)) {
                    fragmentedSteps.add(new PickleStep(stp.getText(), step.getArgument(), step.getLocations()));
                }

            } else {
                fragmentedSteps.add(step);
            }
        }

        return fragmentedSteps;
    }

    public List<Step> getSteps(List<CucumberFeature> cucumberFeatures, String scenarioName) {
        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();
            List<ScenarioDefinition> scenarioDefinitions = feature.getChildren();
            for (ScenarioDefinition scenarioDefinition : scenarioDefinitions) {
                if (scenarioDefinition.getName().equals(scenarioName)) {
                    return scenarioDefinition.getSteps();
                }
            }
        }

        // TODO правильный эррор
        throw new RuntimeException("Cant find scenario fragment with name" + scenarioName);
    }
}
