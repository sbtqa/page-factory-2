package ru.sbtqa.tag.pagefactory.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import java.util.List;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.stepdefs.GenericStepDefs;

public class StepDefs extends GenericStepDefs {

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:user |he |)(?:is on the page|page is being opened|master tab is being opened) \"(.*?)\"$")
    public void openPage(String title) throws PageInitializationException {
        super.openPage(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\)$")
    public void userActionNoParams(String action) throws PageInitializationException, NoSuchMethodException {
        super.userActionNoParams(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) (?:with param |)\"([^\"]*)\"$")
    public void userActionOneParam(String action, String param) throws PageInitializationException, NoSuchMethodException {
        super.userActionOneParam(action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) (?:with the parameters |)\"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionTwoParams(String action, String param1, String param2) throws PageInitializationException, NoSuchMethodException {
        super.userActionTwoParams(action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) (?:with the parameters |)\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionThreeParams(String action, String param1, String param2, String param3) throws PageInitializationException, NoSuchMethodException {
        super.userActionThreeParams(action, param1, param2, param3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) data$")
    public void userActionTableParam(String action, DataTable dataTable) throws PageInitializationException, NoSuchMethodException {
        super.userActionTableParam(action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) [^\"]*\"([^\"]*) data$")
    public void userDoActionWithObject(String action, String param, DataTable dataTable) throws PageInitializationException, NoSuchMethodException {
        super.userDoActionWithObject(action, param, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) from the list$")
    public void userActionListParam(String action, List<String> list) throws PageInitializationException, NoSuchMethodException {
        super.userActionListParam(action, list);
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
    @And("^element \"([^\"]*)\" is focused$")
    public void isElementFocused(String element) {
        super.isElementFocused(element);
    }
}
