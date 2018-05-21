package ru.sbtqa.tag.pagefactory.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import java.util.List;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.stepdefs.GenericStepDefs;
import ru.sbtqa.tag.pagefactory.stepdefs.SetupStepDefs;

public class StepDefs extends GenericStepDefs {

    @Before(order = 0)
    public static void preSetUp(Scenario scenario) {
        SetupStepDefs.preSetUp(scenario);
    }

    @Before(order = 99999)
    public static void setUp(Scenario scenario) {
        SetupStepDefs.setUp(scenario);
    }

    @After(order = 99999)
    public static void tearDown() {
        SetupStepDefs.tearDown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:user |he |)(?:is on the page|page is being opened|master tab is being opened) \"(.*?)\"$")
    public void openPage(String title) throws PageInitializationException {
        super.openPage(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\)$")
    public void userActionNoParams(String action) throws NoSuchMethodException {
        super.userActionNoParams(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) (?:with param |)\"([^\"]*)\"$")
    public void userActionOneParam(String action, String param) throws NoSuchMethodException {
        super.userActionOneParam(action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) (?:with the parameters |)\"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionTwoParams(String action, String param1, String param2) throws NoSuchMethodException {
        super.userActionTwoParams(action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) (?:with the parameters |)\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionThreeParams(String action, String param1, String param2, String param3) throws NoSuchMethodException {
        super.userActionThreeParams(action, param1, param2, param3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) data$")
    public void userActionTableParam(String action, DataTable dataTable) throws NoSuchMethodException {
        super.userActionTableParam(action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) [^\"]*\"([^\"]*) data$")
    public void userDoActionWithObject(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        super.userDoActionWithObject(action, param, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) from the list$")
    public void userActionListParam(String action, List<String> list) throws NoSuchMethodException {
        super.userActionListParam(action, list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^element \"([^\"]*)\" is focused$")
    public void isElementFocused(String element) {
        super.isElementFocused(element);
    }
}
