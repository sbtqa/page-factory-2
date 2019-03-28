package ru.sbtqa.tag.pagefactory.data;

import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.Scenario;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import gherkin.ast.Tag;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.sbtqa.tag.datajack.exceptions.DataException;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import static ru.sbtqa.tag.datajack.providers.AbstractDataProvider.PATH_PARSE_REGEX;

public class DataUtils {

    private static final Configuration PROPERTIES = Configuration.create();
    private static final String DATA_TAG = "@data=";
    private static final String COLLECTION_SIGNATURE = "$";

    public static String replaceDataPlaceholders(String raw, String currentScenarioData) throws DataException {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(raw);
        StringBuilder replacedStep = new StringBuilder(raw);

        while (stepDataMatcher.find()) {
            String collection = stepDataMatcher.group(1);
            String value = stepDataMatcher.group(2);

            if (collection == null) {
                DataUtils.parseDataTagValue(currentScenarioData);
            }

            String builtPath = COLLECTION_SIGNATURE + (collection == null ? "" : collection) + value;
            String parsedValue = DataFactory.getDataProvider().getByPath(builtPath).getValue();
            replacedStep = replacedStep.replace(stepDataMatcher.start(), stepDataMatcher.end(), parsedValue);
            stepDataMatcher = stepDataPattern.matcher(replacedStep);
        }
        return replacedStep.toString();
    }

    public static boolean isDataParameter(String dataParameter) {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(dataParameter);
        return stepDataMatcher.find();
    }

    public static String formFeatureData(CucumberFeature cucumberFeature) {
        String featureDataTagValue = COLLECTION_SIGNATURE + PROPERTIES.getDataInitialCollection();
        String tag = parseDataTags(getFeature(cucumberFeature).getTags());
        return tag == null ? featureDataTagValue : tag;
    }

    public static String formScenarioDataTag(ScenarioDefinition scenarioDefinition, String featureDataTag) {
        List<Tag> currentScenarioTags = DataUtils.getScenarioTags(scenarioDefinition);
        String scenarioDataTagValue = DataUtils.parseDataTags(currentScenarioTags);
        return scenarioDataTagValue == null ? featureDataTag : scenarioDataTagValue;
    }

    public static String formScenarioDataTag(ScenarioDefinition scenarioDefinition) {
        List<Tag> currentScenarioTags = DataUtils.getScenarioTags(scenarioDefinition);
        return DataUtils.parseDataTags(currentScenarioTags);
    }

    private static String parseDataTags(List<Tag> tags) {
        Optional<Tag> dataTag = tags.stream()
                .filter(predicate -> predicate.getName().startsWith(DATA_TAG))
                .findFirst();

        return dataTag.isPresent() ? dataTag.get().getName().split("=")[1].trim() : null;
    }

    public static List<ScenarioDefinition> getScenarioDefinitions(CucumberFeature cucumberFeature) {
        return getFeature(cucumberFeature).getChildren();
    }

    public static List<Tag> getScenarioTags(ScenarioDefinition scenarioDefinition) {
        List<Tag> tags = Collections.EMPTY_LIST;
        if (scenarioDefinition instanceof Scenario) {
            tags = ((Scenario) scenarioDefinition).getTags();
        } else if (scenarioDefinition instanceof ScenarioOutline) {
            tags = ((ScenarioOutline) scenarioDefinition).getTags();
        }
        return tags;
    }

    public static void parseDataTagValue(String tag) throws DataException {
        DataFactory.updateCollection(DataFactory.getDataProvider().getByPath(tag));
    }

    private static Feature getFeature(CucumberFeature cucumberFeature) {
        GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
        return gherkinDocument.getFeature();
    }
}
