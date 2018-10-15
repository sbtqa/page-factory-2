package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.stepdefs.WebGenericSteps;

public class WebSteps extends WebGenericSteps {

    @Override
    @Before(order = 1)
    public void initWeb() {
        super.initWeb();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^copy of the page is being opened in a new tab$")
    public void openCopyPage() {
        super.openCopyPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user switches to the next tab$")
    public void switchesToNextTab() {
        super.switchesToNextTab();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^URL matches \"(.*?)\"$")
    public void urlMatches(String url) {
        super.urlMatches(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user closes the current window and returns to \"(.*?)\"$")
    public void closingCurrentWin(String title) {
        super.closingCurrentWin(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user push back in the browser$")
    public void backPage() {
        super.backPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user navigates to page \"(.*?)\"$")
    public void goToUrl(String url) {
        super.goToUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user navigates to url \"(.*?)\"$")
    public void goToPageByUrl(String url) throws PageInitializationException {
        super.goToPageByUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user refreshes the page$")
    public void reInitPage() {
        super.reInitPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user accepts alert with text \"(.*?)\"$")
    public void acceptAlert(String text) throws WaitException {
        super.acceptAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user dismisses alert with text \"(.*?)\"$")
    public void dismissAlert(String text) throws WaitException {
        super.dismissAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that text \"(.*?)\" appears on the page$")
    public void checkTextAppears(String text) throws WaitException {
        super.checkTextAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that text \"(.*?)\" is absent on the page$")
    public void checkTextIsNotPresent(String text) {
        super.checkTextIsNotPresent(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that modal window with text \"(.*?)\" is appears$")
    public void checkModalWindowAppears(String text) throws WaitException {
        super.checkModalWindowAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user checks that element with text \"(.*?)\" is present$")
    @When("^user checks that the text \"(.*?)\" is visible$")
    public void checkElementWithTextIsPresent(String text) {
        super.checkElementWithTextIsPresent(text);
    }
}
