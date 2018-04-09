package ru.sbtqa.tag.pagefactory.context;

import cucumber.api.Scenario;

public class ScenarioContext {

    private static Scenario scenario;

    private ScenarioContext() {}

    public static Scenario getScenario() {
        return scenario;
    }

    public static void setScenario(Scenario scenario) {
        ScenarioContext.scenario = scenario;
    }
}
