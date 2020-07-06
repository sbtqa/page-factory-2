package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import io.cucumber.datatable.DataTable;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.html.junit.HtmlSetupSteps;
import ru.sbtqa.tag.pagefactory.html.junit.HtmlSteps;
import ru.sbtqa.tag.pagefactory.transformer.ContainCondition;
import ru.sbtqa.tag.pagefactory.transformer.Presence;

import java.util.List;

public class HtmlStepDefs {

    private final HtmlSteps htmlSteps = HtmlSteps.getInstance();

    @Before(order = 2)
    public void initHtml() {
        HtmlSetupSteps.initHtml();
    }

    @After(order = 2)
    public void tearDown() {
        HtmlSetupSteps.tearDown();
    }

    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\)$")
    public void actionInBlock(String block, String action) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action);
    }

    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\) with the parameters of table$")
    public void actionInBlock(String block, String action, DataTable dataTable) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action, dataTable);
    }

    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\) with a parameter \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action, param);
    }

    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\) with the parameters \"([^\"]*)\" \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param1, String param2) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action, param1, param2);
    }

    @And("^user in list \"([^\"]*)\" finds the value element \"([^\"]*)\"$")
    public void find(String listTitle, String value) throws PageException {
        htmlSteps.find(listTitle, value);
    }

    @Когда("^user fills the form$")
    @И("^user marks controls$")
    public void fillForm(DataTable dataTable) {
        htmlSteps.fillForm(dataTable);
    }

    @Когда("^element \"([^\"]*)\" necessarily (is|not) present on the page$")
    public void elementAlwaysPresent(String elementName, Presence present) {
        htmlSteps.elementAlwaysPresent(present, elementName);
    }

    @Когда("^elements necessarily (is|not) present on the page$")
    public void elementsAlwaysPresent(Presence present, List<String> elementNames) {
        htmlSteps.elementsAlwaysPresent(present, elementNames);
    }

    @Когда("^elements is (not )?editable$")
    public void elementsPresentAndDisabled(ContainCondition condition, List<String> elementNames) {
        htmlSteps.elementsPresentAndDisabled(condition, elementNames);
    }

    @Когда("^element \"([^\"]*)\" is (not )?editable$")
    public void elementPresentAndDisabled(String elementName, ContainCondition condition ) {
        htmlSteps.elementPresentAndDisabled(elementName, condition );
    }

    @Когда("^button \"([^\"]*)\" is (not )?selected$")
    public void elementsPresentAndSelected(String elementName, ContainCondition condition) {
        htmlSteps.elementsPresentAndSelected(condition, elementName);
    }

    @Когда("^stores the value of the element \"([^\"]*)\" in the variable \"([^\"]*)\"$")
    public void putInStash(String elementName, String variableName) {
        htmlSteps.putInStash(elementName, variableName);
    }

    @Когда("^hovers over an element \"([^\"]*)\"$")
    public void moveToElement(String elementName) {
        htmlSteps.moveToElement(elementName);
    }

    @Когда("^list of text values is displayed:?$")
    public void checkTextList(List<String> textList) {
        htmlSteps.checkTextList(textList);
    }

    @И("^text is displayed$")
    @Когда("^text is displayed \"([^\"]*)\"$")
    public void checkText(String expectedText) {
        htmlSteps.checkText(expectedText);
    }

    @Когда("^open page \"([^\"]*)\" and wait for element \"([^\"]*)\"$")
    public void openPage(String pageName, String elementName) throws PageInitializationException {
        htmlSteps.openPage(pageName, elementName);
    }

    @Когда("^checks form$")
    public void checkForm(DataTable dataTable) {
        htmlSteps.checkForm(dataTable);
    }

    @И("^text of the element \"([^\"]*)\" contains a fragment$")
    @Когда("^text of the element \"([^\"]*)\" contains a fragment \"([^\"]*)\"$")
    public void checkElementText(String elementName, String expectedText) {
        htmlSteps.checkElementText(elementName, expectedText);
    }

    @И("^text of the element \"([^\"]*)\" is strictly equal$")
    @Когда("^text of the element \"([^\"]*)\" is strictly equal \"([^\"]*)\"$")
    public void checkTextEquals(String elementName, String expectedText) {
        htmlSteps.checkTextEquals(elementName, expectedText);
    }

    @Когда("^text of the element \"([^\"]*)\" matches the mask \"([^\"]*)\"$")
    public void checkInputMask(String elementName, String mask) {
        htmlSteps.checkInputMask(elementName, mask);
    }

    @Когда("^user clicks on the overlapped element \"([^\"]*)\"$")
    public void clickByOverlapElement(String elementName) {
        htmlSteps.clickByOverlapElement(elementName);
    }
}
