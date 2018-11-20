package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.stepdefs.WebGenericSteps;
import ru.sbtqa.tag.stepdefs.WebSetupSteps;

public class WebStepDefs extends WebGenericSteps<WebStepDefs> {

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
    public WebStepDefs openCopyPage() {
        return super.openCopyPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user switches to the next tab$")
    public WebStepDefs switchesToNextTab() {
        return super.switchesToNextTab();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^URL matches \"([^\"]*)\"$")
    public WebStepDefs urlMatches(String url) {
        return super.urlMatches(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user closes the current window and returns to \"([^\"]*)\"$")
    public WebStepDefs closingCurrentWin(String title) {
        return super.closingCurrentWin(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user push back in the browser$")
    public WebStepDefs backPage() {
        return super.backPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user navigates to page \"([^\"]*)\"$")
    public WebStepDefs goToUrl(String url) {
        return super.goToUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user navigates to url \"([^\"]*)\"$")
    public WebStepDefs goToPageByUrl(String url) throws PageInitializationException {
        return super.goToPageByUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user refreshes the page$")
    public WebStepDefs reInitPage() {
        return super.reInitPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user accepts alert with text \"([^\"]*)\"$")
    public WebStepDefs acceptAlert(String text) throws WaitException {
        return super.acceptAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user dismisses alert with text \"([^\"]*)\"$")
    public WebStepDefs dismissAlert(String text) throws WaitException {
        return super.dismissAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that text \"([^\"]*)\" appears on the page$")
    public WebStepDefs checkTextAppears(String text) throws WaitException {
        return super.checkTextAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that text \"([^\"]*)\" is absent on the page$")
    public WebStepDefs checkTextIsNotPresent(String text) {
        return super.checkTextIsNotPresent(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that modal window with text \"([^\"]*)\" is appears$")
    public WebStepDefs checkModalWindowAppears(String text) throws WaitException {
        return super.checkModalWindowAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that element with text \"([^\"]*)\" is present$")
    @When("^user checks that the text \"([^\"]*)\" is visible$")
    public WebStepDefs checkElementWithTextIsPresent(String text) {
        return super.checkElementWithTextIsPresent(text);
    }
}
