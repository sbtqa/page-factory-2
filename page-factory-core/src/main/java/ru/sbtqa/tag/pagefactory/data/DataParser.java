package ru.sbtqa.tag.pagefactory.data;

import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.Examples;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import gherkin.ast.Step;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.datajack.exceptions.DataException;

public class DataParser {

    public void replaceDataPlaceholders(List<CucumberFeature> cucumberFeatures) throws DataException, IllegalAccessException {
        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            String featureDataTag = DataUtils.formFeatureData(cucumberFeature);

            List<ScenarioDefinition> featureChildren = DataUtils.getScenarioDefinitions(cucumberFeature);

            for (ScenarioDefinition scenarioDefinition : featureChildren) {
                String scenarioDataTagValue = DataUtils.formScenarioDataTag(scenarioDefinition, featureDataTag);
                List<Step> steps = scenarioDefinition.getSteps();

                if (scenarioDefinition instanceof ScenarioOutline) {
                    List<Examples> examples = ((ScenarioOutline) scenarioDefinition).getExamples();
                    FieldUtils.writeField(scenarioDefinition, "examples", DataUtils.replaceExamplesPlaceholders(examples, scenarioDataTagValue), true);
                }

                for (Step step : steps) {
                    FieldUtils.writeField(step, "argument", DataUtils.replaceArgumentPlaceholders(step.getArgument(), scenarioDataTagValue), true);
                    FieldUtils.writeField(step, "text", DataUtils.replaceDataPlaceholders(step.getText(), scenarioDataTagValue), true);
                }
            }
        }
    }
}