package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import java.util.List;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.junit.CoreSetupSteps;
import ru.sbtqa.tag.pagefactory.junit.CoreSteps;
import ru.sbtqa.tag.pagefactory.transformer.ContainCondition;

public class CoreStepDefs {

    private final CoreSteps coreSteps = CoreSteps.getInstance();

    @Before(order = 0)
    public void preSetUp() {
        CoreSetupSteps.preSetUp();
    }

    @Before(order = 99999)
    public void setUp(Scenario scenario) {
        Environment.setScenario(scenario);
        CoreSetupSteps.setUp();
    }

    @After(order = 1)
    public void tearDown() {
        CoreSetupSteps.tearDown();
    }

    @And("(?:user |he )?(?:is on the page|page is being opened|master tab is being opened) \"([^\"]*)\"$")
    public void openPage(String title) throws PageInitializationException {
        coreSteps.openPage(title);
    }

    @And("^(?:user |he )?(?:is on the page|page is being opened|master tab is being opened) \"([^\"]*)\" with data$")
    public void openPage(String title, DataTable dataTable) throws PageInitializationException {
        coreSteps.openPage(title, dataTable);
    }

    @And("^user \\(([^)]*)\\)$")
    public void action(String action) throws NoSuchMethodException {
        coreSteps.action(action);
    }

    @And("^user \\(([^)]*)\\) (?:with param )?\"([^\"]*)\"$")
    public void action(String action, String param) throws NoSuchMethodException {
        coreSteps.action(action, param);
    }

    @And("^user \\(([^)]*)\\) (?:with the parameters )?\"([^\"]*)\" \"([^\"]*)\"$")
    public void action(String action, String param1, String param2) throws NoSuchMethodException {
        coreSteps.action(action, param1, param2);
    }

    @And("^user \\(([^)]*)\\) (?:with the parameters )?\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void action(String action, String param1, String param2, String param3) throws NoSuchMethodException {
        coreSteps.action(action, param1, param2, param3);
    }

    @And("^user \\(([^)]*)\\) data$")
    public void action(String action, DataTable dataTable) throws NoSuchMethodException {
        coreSteps.action(action, dataTable);
    }

    @And("^user \\(([^)]*)\\) \"([^\"]*)\" data$")
    public void action(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        coreSteps.action(action, param, dataTable);
    }

    @And("^user \\(([^)]*)\\) from the list$")
    public void action(String action, List<String> list) throws NoSuchMethodException {
        coreSteps.action(action, list);
    }

    @When("^user fills the field \"([^\"]*)\"(?: with value)?$")
    @And("^user fills the field \"([^\"]*)\" (?:with value )?\"([^\"]*)\"$")
    public void fill(String elementTitle, String text) throws PageException {
        coreSteps.fill(elementTitle, text);
    }

    @And("^user clicks the link \"([^\"]*)\"$")
    @When("^user clicks the button \"([^\"]*)\"$")
    public void click(String elementTitle) throws PageException {
        coreSteps.click(elementTitle);
    }

    @And("^user presses the key \"([^\"]*)\"$")
    public void pressKey(String keyName) {
        coreSteps.pressKey(keyName);
    }

    @And("^user presses the key \"([^\"]*)\" on the element \"([^\"]*)\"$")
    public void pressKey(String keyName, String elementTitle) throws PageException {
        coreSteps.pressKey(keyName, elementTitle);
    }

    @And("^user selects in \"([^\"]*)\" the value \"([^\"]*)\"$")
    public void select(String elementTitle, String option) throws PageException {
        coreSteps.select(elementTitle, option);
    }

    @And("^user selects the checkbox \"([^\"]*)\"$")
    public void setCheckBox(String elementTitle) throws PageException {
        coreSteps.setCheckBox(elementTitle);
    }

    @When("^user checks in the element \"([^\"]*)\" value \"([^\"]*)\"$")
    @And("^user checks in the element \"([^\"]*)\" value$")
    public void checkValueIsEqual(String elementTitle, String text) throws PageException {
        coreSteps.checkValueIsEqual(elementTitle, text);
    }

    @And("^user checks in the element \"([^\"]*)\" that the value is not equal \"([^\"]*)\"$")
    public void checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        coreSteps.checkValueIsNotEqual(elementTitle, text);
    }

    @And("^user checks value in \"([^\"]*)\" contains \"([^\"]*)\"$")
    public void checkValueContains(String elementTitle, String text) throws PageException {
        coreSteps.checkValueContains(elementTitle, text);
    }

    @And("^user checks value in \"([^\"]*)\" not contains \"([^\"]*)\"$")
    public void checkValueNotContains(String elementTitle, String text) throws PageException {
        coreSteps.checkValueNotContains(elementTitle, text);
    }

    @And("^user checks that the field \"([^\"]*)\" is not empty$")
    public void checkNotEmpty(String elementTitle) throws PageException {
        coreSteps.checkNotEmpty(elementTitle);
    }

    @And("^user checks that the field \"([^\"]*)\" is empty$")
    public void checkEmpty(String elementTitle) throws PageException {
        coreSteps.checkEmpty(elementTitle);
    }

    @And("^element \"([^\"]*)\" is focused$")
    public void isElementFocused(String element) {
        coreSteps.isElementFocused(element);
    }

    @And("^(?:user |he )?inserts fragment \"([^\"]*)\"$")
    @When("^(?:user |he )?performs \"([^\"]*)\"$")
    @Then("^(?:user |he |)performs \"([^\"]*)\" scenario$")
    public void userInsertsFragment(String fragmentName) throws FragmentException {
            coreSteps.userInsertsFragment(fragmentName);
    }

    @And("^(?:user |he )?is waiting for the element to appear \"([^\"]*)\"$")
    public void appearElement(String elementName) throws PageException {
        coreSteps.appearElement(elementName);
    }

    @And("^(?:user |he )?is waiting (\\d+) second(?:s)? for the element to appear \"([^\"]*)\"$")
    public void appearElement(int timeout, String elementName) throws PageException {
        coreSteps.appearElement(timeout, elementName);
    }

    @And("^(?:user |he )?is waiting for the element to disappear \"([^\"]*)\"$")
    public void waitInvisibility(String elementName) throws PageException {
        coreSteps.waitInvisibility(elementName);
    }

    @And("^(?:user |he )?is waiting (\\d+) second(?:s)? for the element to disappear \"([^\"]*)\"$")
    public void waitInvisibility(int timeout, String elementName) throws PageException {
        coreSteps.waitInvisibility(timeout, elementName);
    }

    @And("^(?:user |he )?is waiting for the value of the \"([^\"]*)\" attribute of the element \"([^\"]*)\" to become \"([^\"]*)\"$")
    public void waitChangeAttribute(String attribute, String elementName, String attributeValue) throws PageException {
        coreSteps.waitChangeAttribute(attribute, elementName, attributeValue);
    }

    @And("^(?:user |he )?is waiting (\\d+) second(?:s)? for the value of the \"([^\"]*)\" attribute of the element \"([^\"]*)\" to become \"([^\"]*)\"$")
    public void waitChangeAttribute(int timeout, String attribute, String elementName, String attributeValue) throws PageException {
        coreSteps.waitChangeAttribute(timeout, attribute, elementName, attributeValue);
    }

    @And("^(?:user |he )?is waiting for the value of the \"([^\"]*)\" attribute of the element \"([^\"]*)\" should (not contain|contain) \"([^\"]*)\"$")
    public void waitAttributeContains(String attribute, String elementName, ContainCondition condition, String partAttributeValue) throws PageException {
        coreSteps.waitAttributeContains(attribute, elementName, condition, partAttributeValue);
    }

    @And("^(?:user |he )?is waiting (\\d+) second(?:s)? for the value of the \"([^\"]*)\" attribute of the element \"([^\"]*)\" should (not contain|contain) \"([^\"]*)\"$")
    public void waitAttributeContains(int timeout, String attribute, String elementName, ContainCondition condition, String partAttributeValue) throws PageException {
        coreSteps.waitAttributeContains(timeout, attribute, elementName, condition, partAttributeValue);
    }

    @And("^(?:user |he )?is waiting for the element \"([^\"]*)\" to (not contain|contain) text \"([^\"]*)\"$")
    public void waitElementContainsText(String elementName, ContainCondition condition, String text) throws PageException {
        coreSteps.waitElementContainsText(elementName, condition, text);
    }

    @And("^(?:user |he )?is waiting (\\d+) second(?:s)? for the element \"([^\"]*)\" to (not contain|contain) text \"([^\"]*)\"$")
    public void waitElementContainsText(int timeout, String elementName, ContainCondition condition, String text) throws PageException {
        coreSteps.waitElementContainsText(timeout, elementName, condition, text);
    }

    @And("^(?:user |he )?is waiting for the element \"([^\"]*)\" to become clickable$")
    public void waitClickability(String elementName) throws PageException {
        coreSteps.waitClickability(elementName);
    }

    @And("^(?:user |he )?is waiting (\\d+) second(?:s)? for the element \"([^\"]*)\" to become clickable$")
    public void waitClickability(int timeout, String elementName) throws PageException {
        coreSteps.waitClickability(timeout, elementName);
    }

    @And("^user accepts alert with text \"([^\"]*)\"$")
    public void acceptAlert(String text) throws WaitException {
        coreSteps.acceptAlert(text);
    }

    @And("^user dismisses alert with text \"([^\"]*)\"$")
    public void dismissAlert(String text) throws WaitException {
        coreSteps.dismissAlert(text);
    }

    @And("^user clears the field \"([^\"]*)\"$")
    public void clearField(String elementTitle) throws PageException {
        coreSteps.clearField(elementTitle);
    }
}
