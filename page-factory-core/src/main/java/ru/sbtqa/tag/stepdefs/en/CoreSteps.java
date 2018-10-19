package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import java.util.List;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.stepdefs.CoreGenericSteps;

public class CoreSteps extends CoreGenericSteps {

    @Override
    @Before(order = 0)
    public void preSetUp(Scenario scenario) {
        super.preSetUp(scenario);
    }

    @Override
    @Before(order = 99999)
    public void setUp(Scenario scenario) {
        super.setUp(scenario);
    }

    @Override
    @After(order = 99999)
    public void tearDown() {
        super.tearDown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:user |he )?(?:is on the page|page is being opened|master tab is being opened) \"([^\"]*)\"$")
    public void openPage(String title) throws PageInitializationException {
        super.openPage(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\(([^)]*)\\)$")
    public void userActionNoParams(String action) throws NoSuchMethodException {
        super.userActionNoParams(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\(([^)]*)\\) (?:with param )?\"([^\"]*)\"$")
    public void userActionOneParam(String action, String param) throws NoSuchMethodException {
        super.userActionOneParam(action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\(([^)]*)\\) (?:with the parameters )?\"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionTwoParams(String action, String param1, String param2) throws NoSuchMethodException {
        super.userActionTwoParams(action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\(([^)]*)\\) (?:with the parameters )?\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionThreeParams(String action, String param1, String param2, String param3) throws NoSuchMethodException {
        super.userActionThreeParams(action, param1, param2, param3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\(([^)]*)\\) data$")
    public void userActionTableParam(String action, DataTable dataTable) throws NoSuchMethodException {
        super.userActionTableParam(action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\(([^)]*)\\) \"([^\"]*)\" data$")
    public void userDoActionWithObject(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        super.userDoActionWithObject(action, param, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\(([^)]*)\\) from the list$")
    public void userActionListParam(String action, List<String> list) throws NoSuchMethodException {
        super.userActionListParam(action, list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user fills the field \"([^\"]*)\" (?:with value )?\"([^\"]*)\"$")
    public void fill(String elementTitle, String text) throws PageException {
        super.fill(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user clicks the link \"([^\"]*)\"$")
    @When("^user clicks the button \"([^\"]*)\"$")
    public void click(String elementTitle) throws PageException {
        super.click(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user presses the key \"([^\"]*)\"$")
    public void pressKey(String keyName) {
        super.pressKey(keyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user presses the key \"([^\"]*)\" on the element \"([^\"]*)\"$")
    public void pressKey(String keyName, String elementTitle) throws PageException {
        super.pressKey(keyName, elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user selects in \"([^\"]*)\" the value \"([^\"]*)\"$")
    public void select(String elementTitle, String option) throws PageException {
        super.select(elementTitle, option);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user selects the checkbox \"([^\"]*)\"$")
    public void setCheckBox(String elementTitle) throws PageException {
        super.setCheckBox(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks in the element \"([^\"]*)\" value \"([^\"]*)\"$")
    public void checkValueIsEqual(String elementTitle, String text) throws PageException {
        super.checkValueIsEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks in the element \"([^\"]*)\" that the value is not equal \"([^\"]*)\"$")
    public void checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        super.checkValueIsNotEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that the field \"([^\"]*)\" is not empty$")
    public void checkNotEmpty(String elementTitle) throws PageException {
        super.checkNotEmpty(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that the field \"([^\"]*)\" is empty$")
    public void checkEmpty(String elementTitle) throws PageException {
        super.checkEmpty(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^element \"([^\"]*)\" is focused$")
    public void isElementFocused(String element) {
        super.isElementFocused(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:user |he )?inserts fragment \"([^\"]*)\"$")
    public void userInsertsFragment(String fragmentName) throws FragmentException {
        super.userInsertsFragment(fragmentName);
    }
}
