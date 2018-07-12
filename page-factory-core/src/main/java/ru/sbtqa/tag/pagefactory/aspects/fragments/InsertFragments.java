package ru.sbtqa.tag.pagefactory.aspects.fragments;

import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Step;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class InsertFragments {

    public static final String INSERT_FRAGMENT = "^(?:user |he |)\\(insert fragment\\) \"([^\"]*)\"$";

    @Pointcut("call(* *.addChildren(..))")
    public void addChildren() {
    }

    @Around("addChildren()")
    public void stash(ProceedingJoinPoint joinPoint) throws Throwable {
        List<CucumberFeature> cucumberFeatures = (List<CucumberFeature>) joinPoint.getArgs()[0];
        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();
            List<ScenarioDefinition> featureChildren = feature.getChildren();
            for (ScenarioDefinition scenarioDefinition : featureChildren) {
                List<Step> steps = scenarioDefinition.getSteps();

                FieldUtils.writeField(scenarioDefinition, "steps", replaceOnFragments(steps), true);
            }
        }

        joinPoint.proceed();
    }

    /**
     * Обойти переданный список шагов и если найден шаг который должен быть заменен на фрагмент, тогда взять название
     * сценария, найти его шаги и подставить их вместо найденного шага, требующего замены на фрагмент
     *
     * @param steps список шагов в которых производится поиск шага требующего замены на фрагмент
     * @return список шагов с подставленными фрагментами
     * @throws IllegalAccessException ???
     */
    private List<Step> replaceOnFragments(List<Step> steps) throws IllegalAccessException {
        List<Step> fragmentedSteps = new ArrayList<>();

        for (Step step : steps) {
            if (isFragmentRequire(step)) {
                String requiredFragmentName = getRequiredFragmentName(step);

                List<CucumberFeature> fragmentFeatures = getFragmentFeatures();
                for (Step fragmentStep : getFragmentSteps(fragmentFeatures, requiredFragmentName)) {
                    copyLocation(step, fragmentStep);
                    fragmentedSteps.add(fragmentStep);
                }
            } else {
                fragmentedSteps.add(step);
            }
        }

        return fragmentedSteps;
    }

    private void copyLocation(Step originalStep, Step targetStep) throws IllegalAccessException {
        FieldUtils.writeField(targetStep, "location", originalStep.getLocation(), true);
    }

    /**
     * Находит список шагов сценария, который будет подставляться как фрагмент
     *
     * @param cucumberFeatures список фич где будет происходить поиск
     * @param scenarioName имя сценария
     * @return список шагов найденного сценария
     */
    private List<Step> getFragmentSteps(List<CucumberFeature> cucumberFeatures, String scenarioName) {
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

    private List<CucumberFeature> getFragmentFeatures() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        List<String> featurePaths = new ArrayList<>();
        featurePaths.add("src/test/resources/fragmentscenario");

        return CucumberFeature.load(resourceLoader, featurePaths);
    }

    private boolean isFragmentRequire(Step step) {
        return Pattern.matches(INSERT_FRAGMENT, step.getText());
    }

    private String getRequiredFragmentName(Step step) {
        Pattern pattern = Pattern.compile(INSERT_FRAGMENT);
        Matcher matcher = pattern.matcher(step.getText());
        matcher.find();
        return matcher.group(1);
    }
}
