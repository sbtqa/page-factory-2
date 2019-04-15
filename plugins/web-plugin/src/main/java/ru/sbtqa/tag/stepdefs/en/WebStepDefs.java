package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.ru.Когда;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.web.junit.WebSetupSteps;
import ru.sbtqa.tag.pagefactory.web.junit.WebSteps;

public class WebStepDefs {

    private final WebSteps webSteps = WebSteps.getInstance();

    @Before(order = 1)
    public void initWeb() {
        WebSetupSteps.initWeb();
    }

    @After(order = 9999)
    public void disposeWeb() {
        WebSetupSteps.disposeWeb();
    }

    @And("^copy of the page is being opened in a new tab$")
    public void openCopyPage() {
        webSteps.openCopyPage();
    }

    @And("^user switches to the next tab$")
    public void switchesToNextTab() {
        webSteps.switchesToNextTab();
    }

    @And("^URL matches \"([^\"]*)\"$")
    public void urlMatches(String url) {
        webSteps.urlMatches(url);
    }

    @And("^user closes the current window and returns to \"([^\"]*)\"$")
    public void closingCurrentWin(String title) {
        webSteps.closingCurrentWin(title);
    }


    @And("^user push back in the browser$")
    public void backPage() {
        webSteps.backPage();
    }

    @And("^user navigates to url \"([^\"]*)\"$")
    public void goToUrl(String url) {
        webSteps.goToUrl(url);
    }

    @And("^user refreshes the page$")
    public void reInitPage() {
        webSteps.reInitPage();
    }

    @And("^user accepts alert with text \"([^\"]*)\"$")
    public void acceptAlert(String text) throws WaitException {
        webSteps.acceptAlert(text);
    }

    @And("^user dismisses alert with text \"([^\"]*)\"$")
    public void dismissAlert(String text) throws WaitException {
        webSteps.dismissAlert(text);
    }

    @And("^user checks that text \"([^\"]*)\" appears on the page$")
    public void checkTextAppears(String text) throws WaitException, InterruptedException {
        webSteps.checkTextAppears(text);
    }

    @And("^user checks that text \"([^\"]*)\" is absent on the page$")
    public void checkTextIsNotPresent(String text) throws InterruptedException {
        webSteps.checkTextIsNotPresent(text);
    }

    @And("^user checks that modal window with text \"([^\"]*)\" is appears$")
    public void checkModalWindowAppears(String text) throws WaitException {
        webSteps.checkModalWindowAppears(text);
    }

    @And("^user checks that element with text \"([^\"]*)\" is present$")
    @When("^user checks that the text \"([^\"]*)\" is visible$")
    public void checkElementWithTextIsPresent(String text) {
        webSteps.checkElementWithTextIsPresent(text);
    }

    @Когда("^user stores the value of the field \"([^\"]*)\" in the variable \"([^\"]*)\"$")
    public void putElementValueInStash(String elementName, String variableName) throws PageException {
        webSteps.putElementValueInStash(elementName, variableName);
    }
}
