package ru.sbtqa.tag.pagefactory.web.stepdefs.en;

import cucumber.api.java.en.And;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.web.stepdefs.WebGenericStepDefs;

public class WebStepDefs extends WebGenericStepDefs {

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
}
