package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.stepdefs.WebSetupSteps;
import ru.sbtqa.tag.stepdefs.WebSteps;

public class WebStepDefs extends WebSteps {

    @Before(order = 1)
    public void initWeb() {
        WebSetupSteps.initWeb();
    }

    @After(order = 9999)
    public void disposeWeb() {
        WebSetupSteps.disposeWeb();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @And("^copy of the page is being opened in a new tab$")
    public WebSteps openCopyPage() {
        return super.openCopyPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user switches to the next tab$")
    public WebSteps switchesToNextTab() {
        return super.switchesToNextTab();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^URL matches \"([^\"]*)\"$")
    public WebSteps urlMatches(String url) {
        return super.urlMatches(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user closes the current window and returns to \"([^\"]*)\"$")
    public WebSteps closingCurrentWin(String title) {
        return super.closingCurrentWin(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user push back in the browser$")
    public WebSteps backPage() {
        return super.backPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user navigates to page \"([^\"]*)\"$")
    public WebSteps goToUrl(String url) {
        return super.goToUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user navigates to url \"([^\"]*)\"$")
    public WebSteps goToPageByUrl(String url) throws PageInitializationException {
        return super.goToPageByUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user refreshes the page$")
    public WebSteps reInitPage() {
        return super.reInitPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user accepts alert with text \"([^\"]*)\"$")
    public WebSteps acceptAlert(String text) throws WaitException {
        return super.acceptAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user dismisses alert with text \"([^\"]*)\"$")
    public WebSteps dismissAlert(String text) throws WaitException {
        return super.dismissAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that text \"([^\"]*)\" appears on the page$")
    public WebSteps checkTextAppears(String text) throws WaitException {
        return super.checkTextAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that text \"([^\"]*)\" is absent on the page$")
    public WebSteps checkTextIsNotPresent(String text) {
        return super.checkTextIsNotPresent(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that modal window with text \"([^\"]*)\" is appears$")
    public WebSteps checkModalWindowAppears(String text) throws WaitException {
        return super.checkModalWindowAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that element with text \"([^\"]*)\" is present$")
    @When("^user checks that the text \"([^\"]*)\" is visible$")
    public WebSteps checkElementWithTextIsPresent(String text) {
        return super.checkElementWithTextIsPresent(text);
    }
}
