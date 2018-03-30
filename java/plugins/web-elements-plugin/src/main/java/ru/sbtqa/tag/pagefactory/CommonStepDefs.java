package ru.sbtqa.tag.pagefactory;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import io.qameta.allure.cucumber2jvm.CucumberSourceUtils;

public class CommonStepDefs {

    @Before(order = 1)
    public void before(Scenario scenario) {
        System.out.println(scenario);
        CucumberSourceUtils cucumberSourceUtils = new CucumberSourceUtils();
    }

}
