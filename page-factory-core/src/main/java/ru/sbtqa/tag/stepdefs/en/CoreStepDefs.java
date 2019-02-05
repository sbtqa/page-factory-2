package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.Transform;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.ru.Когда;
import java.util.List;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.transformer.ConditionTransformer;
import ru.sbtqa.tag.pagefactory.transformer.enums.Condition;
import ru.sbtqa.tag.stepdefs.CoreGenericSteps;
import ru.sbtqa.tag.stepdefs.CoreSetupSteps;

public class CoreStepDefs extends CoreGenericSteps<CoreStepDefs> {

    @Before(order = 0)
    public void preSetUp() {
        CoreSetupSteps.preSetUp();
    }

    @Before(order = 99999)
    public void setUp() {
        CoreSetupSteps.setUp();
    }

    @After(order = 1)
    public void tearDown() {
        CoreSetupSteps.tearDown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:user |he )?(?:is on the page|page is being opened|master tab is being opened) \"([^\"]*)\"$")
    public CoreStepDefs openPage(String title) throws PageInitializationException {
        return super.openPage(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\(([^)]*)\\)$")
    public CoreStepDefs action(String action) throws NoSuchMethodException {
        return super.action(action);
    }

    /**
     * {@inheritDoc}
     */
    @And("^user \\(([^)]*)\\) (?:with param )?\"([^\"]*)\"$")
    public CoreStepDefs action(String action, String param) throws NoSuchMethodException {
        return super.action(action, param);
    }

    /**
     * {@inheritDoc}
     */
    @And("^user \\(([^)]*)\\) (?:with the parameters )?\"([^\"]*)\" \"([^\"]*)\"$")
    public CoreStepDefs action(String action, String param1, String param2) throws NoSuchMethodException {
        return super.action(action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @And("^user \\(([^)]*)\\) (?:with the parameters )?\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public CoreStepDefs action(String action, String param1, String param2, String param3) throws NoSuchMethodException {
        return super.action(action, param1, param2, param3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\(([^)]*)\\) data$")
    public CoreStepDefs action(String action, DataTable dataTable) throws NoSuchMethodException {
        return super.action(action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\(([^)]*)\\) \"([^\"]*)\" data$")
    public CoreStepDefs action(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        return super.action(action, param, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\(([^)]*)\\) from the list$")
    public CoreStepDefs action(String action, List<String> list) throws NoSuchMethodException {
        return super.action(action, list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @When("^user fills the field \"([^\"]*)\"(?: with value)?$")
    @And("^user fills the field \"([^\"]*)\" (?:with value )?\"([^\"]*)\"$")
    public CoreStepDefs fill(String elementTitle, String text) throws PageException {
        return super.fill(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user clicks the link \"([^\"]*)\"$")
    @When("^user clicks the button \"([^\"]*)\"$")
    public CoreStepDefs click(String elementTitle) throws PageException {
        return super.click(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user presses the key \"([^\"]*)\"$")
    public CoreStepDefs pressKey(String keyName) {
        return super.pressKey(keyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user presses the key \"([^\"]*)\" on the element \"([^\"]*)\"$")
    public CoreStepDefs pressKey(String keyName, String elementTitle) throws PageException {
        return super.pressKey(keyName, elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user selects in \"([^\"]*)\" the value \"([^\"]*)\"$")
    public CoreStepDefs select(String elementTitle, String option) throws PageException {
        return super.select(elementTitle, option);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user selects the checkbox \"([^\"]*)\"$")
    public CoreStepDefs setCheckBox(String elementTitle) throws PageException {
        return super.setCheckBox(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks in the element \"([^\"]*)\" value \"([^\"]*)\"$")
    public CoreStepDefs checkValueIsEqual(String elementTitle, String text) throws PageException {
        return super.checkValueIsEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks in the element \"([^\"]*)\" that the value is not equal \"([^\"]*)\"$")
    public CoreStepDefs checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        return super.checkValueIsNotEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that the field \"([^\"]*)\" is not empty$")
    public CoreStepDefs checkNotEmpty(String elementTitle) throws PageException {
        return super.checkNotEmpty(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that the field \"([^\"]*)\" is empty$")
    public CoreStepDefs checkEmpty(String elementTitle) throws PageException {
        return super.checkEmpty(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^element \"([^\"]*)\" is focused$")
    public CoreStepDefs isElementFocused(String element) {
        return super.isElementFocused(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:user |he )?inserts fragment \"([^\"]*)\"$")
    public CoreStepDefs userInsertsFragment(String fragmentName) throws FragmentException {
        return super.userInsertsFragment(fragmentName);
    }

    @Override
    @Когда("^(?:user |he )?is waiting for the element to appear \"([^\"]*)\"$")
    public CoreStepDefs appearElement(String elementName) throws PageException {
        return super.appearElement(elementName);
    }

    @Override
    @Когда("^(?:user |he )?is waiting (\\d+) second(?:s)? for the element to appear \"([^\"]*)\"$")
    public CoreStepDefs appearElement(int timeout, String elementName) throws PageException {
        return super.appearElement(timeout, elementName);
    }

    @Override
    @Когда("^(?:user |he )?is waiting for the element to disappear \"([^\"]*)\"$")
    public CoreStepDefs waitInvisibility(String elementName) throws PageException {
        return super.waitInvisibility(elementName);
    }

    @Override
    @Когда("^(?:user |he )?is waiting (\\d+) second(?:s)? for the element to disappear \"([^\"]*)\"$")
    public CoreStepDefs waitInvisibility(int timeout, String elementName) throws PageException {
        return super.waitInvisibility(timeout, elementName);
    }

    @Override
    @Когда("^(?:user |he )?is waiting for the value of the \"([^\"]*)\" attribute of the element \"([^\"]*)\" to become \"([^\"]*)\"$")
    public CoreStepDefs waitChangeAttribute(String attribute, String elementName, String attributeValue) throws PageException {
        return super.waitChangeAttribute(attribute, elementName, attributeValue);
    }

    @Override
    @Когда("^(?:user |he )?is waiting (\\d+) second(?:s)? for the value of the \"([^\"]*)\" attribute of the element \"([^\"]*)\" to become \"([^\"]*)\"$")
    public CoreStepDefs waitChangeAttribute(int timeout, String attribute, String elementName, String attributeValue) throws PageException {
        return super.waitChangeAttribute(timeout, attribute, elementName, attributeValue);
    }

    @Override
    @Когда("^(?:user |he )?is waiting for the value of the \"([^\"]*)\" attribute of the element \"([^\"]*)\" should (not )?contain \"([^\"]*)\"$")
    public CoreStepDefs waitAttributeContains(String attribute, String elementName,
            @Transform(ConditionTransformer.class) Condition negation, String partAttributeValue) throws PageException {
        return super.waitAttributeContains(attribute, elementName, negation, partAttributeValue);
    }

    @Override
    @Когда("^(?:user |he )?is waiting (\\d+) second(?:s)? for the value of the \"([^\"]*)\" attribute of the element \"([^\"]*)\" should (not )?contain \"([^\"]*)\"$")
    public CoreStepDefs waitAttributeContains(int timeout, String attribute, String elementName,
            @Transform(ConditionTransformer.class) Condition negation, String partAttributeValue) throws PageException {
        return super.waitAttributeContains(timeout, attribute, elementName, negation, partAttributeValue);
    }

    @Override
    @Когда("^(?:user |he )?is waiting for the element \"([^\"]*)\" to (not )?contain text \"([^\"]*)\"$")
    public CoreStepDefs waitElementContainsText(String elementName, @Transform(ConditionTransformer.class) Condition negation, String text) throws PageException {
        return super.waitElementContainsText(elementName, negation, text);
    }

    @Override
    @Когда("^(?:user |he )?is waiting (\\d+) second(?:s)? for the element \"([^\"]*)\" to (not )?contain text \"([^\"]*)\"$")
    public CoreStepDefs waitElementContainsText(int timeout, String elementName, @Transform(ConditionTransformer.class) Condition negation, String text) throws PageException {
        return super.waitElementContainsText(timeout, elementName, negation, text);
    }
    
    @Override
    @Когда("^(?:user |he )?is waiting for the element \"([^\"]*)\" to become clickable$")
    public CoreStepDefs waitClickability(String elementName) throws PageException {
        return super.waitClickability(elementName);
    }

    @Override
    @Когда("^(?:user |he )?is waiting (\\d+) second(?:s)? for the element \"([^\"]*)\" to become clickable$")
    public CoreStepDefs waitClickability(int timeout, String elementName) throws PageException {
        return super.waitClickability(timeout, elementName);
    }
}
