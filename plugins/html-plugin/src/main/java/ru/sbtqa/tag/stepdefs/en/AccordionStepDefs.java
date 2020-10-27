package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.en.When;
import cucumber.api.java.ru.И;
import io.cucumber.datatable.DataTable;
import ru.sbtqa.tag.pagefactory.html.junit.AccordionSteps;
import ru.sbtqa.tag.pagefactory.transformer.ContainCondition;
import ru.sbtqa.tag.pagefactory.transformer.SearchStrategy;

public class AccordionStepDefs {

    private final AccordionSteps accordionSteps = AccordionSteps.getInstance();

    @When("^user opens the accordion \"([^\"]*)\"$")
    public void open(String accordionName) {
        accordionSteps.open(accordionName);
    }

    @When("^user closes the accordion \"([^\"]*)\"$")
    public void close(String accordionName) {
        accordionSteps.close(accordionName);
    }

    @When("^accordion is (not )?opened \"([^\"]*)\"$")
    public void checkAccordionOpened(ContainCondition condition, String accordionName) {
        accordionSteps.checkAccordionOpened(condition, accordionName);
    }

    @И("^text of the contents of the accordion \"([^\"]*)\" is equal to$")
    @When("^text of the contents of the accordion \"([^\"]*)\" is equal to \"([^\"]*)\"$")
    public void checkContent(String accordionName, String expectedText) {
        accordionSteps.checkContent(accordionName, expectedText, SearchStrategy.EQUALS);
    }

    @И("^text of the contents of the accordion \"([^\"]*)\" contains a fragment$")
    @When("^text of the contents of the accordion \"([^\"]*)\" contains a fragment \"([^\"]*)\"$")
    public void checkContentContains(String accordionName, String expectedText) {
        accordionSteps.checkContent(accordionName, expectedText, SearchStrategy.CONTAINS);
    }

    @И("^text of the header of the accordion \"([^\"]*)\" is equal to$")
    @When("^text of the header of the accordion \"([^\"]*)\" is equal to \"([^\"]*)\"$")
    public void checkHeader(String accordionName, String expectedText) {
        accordionSteps.checkHeader(accordionName, expectedText, SearchStrategy.EQUALS);
    }

    @И("^text of the header of the accordion \"([^\"]*)\" contains a fragment$")
    @When("^text of the header of the accordion \"([^\"]*)\" contains a fragment \"([^\"]*)\"$")
    public void checkHeaderContains(String accordionName, String expectedText) {
        accordionSteps.checkHeader(accordionName, expectedText, SearchStrategy.CONTAINS);
    }

    @И("^text of the element \"([^\"]*)\" in the header of the accordion \"([^\"]*)\" is equal to$")
    @When("^text of the element \"([^\"]*)\" in the header of the accordion \"([^\"]*)\" is equal to \"([^\"]*)\"$")
    public void checkHeaderElement(String elementName, String accordionName, String expectedText) {
        accordionSteps.checkHeaderElement(elementName, accordionName, expectedText);
    }

    @И("^text of the element \"([^\"]*)\" in the content of the accordion \"([^\"]*)\" is equal to$")
    @When("^text of the element \"([^\"]*)\" in the content of the accordion \"([^\"]*)\" is equal to \"([^\"]*)\"$")
    public void checkContentElement(String elementName, String accordionName, String expectedText) {
        accordionSteps.checkContentElement(elementName, accordionName, expectedText);
    }

    @When("^checks text of the elements in the contents of the accordion \"([^\"]*)\"$")
    public void checkElements(String accordionName, DataTable expectedTable) {
        accordionSteps.checkElements(accordionName, expectedTable);
    }
}
