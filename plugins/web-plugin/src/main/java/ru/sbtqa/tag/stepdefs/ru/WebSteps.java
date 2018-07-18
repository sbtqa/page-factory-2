package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.Before;
import cucumber.api.java.ru.И;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.stepdefs.WebGenericSteps;

public class WebSteps extends WebGenericSteps {

    @Before(order = 1)
    public void initWeb() {
        super.initWeb();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^открывается копия страницы в новой вкладке$")
    public void openCopyPage() {
        super.openCopyPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)переключается на соседнюю вкладку$")
    public void switchesToNextTab() {
        super.switchesToNextTab();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^URL соответствует \"(.*?)\"$")
    public void urlMatches(String url) {
        super.urlMatches(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)закрывает текущее окно и возвращается на \"(.*?)\"$")
    public void closingCurrentWin(String title) {
        super.closingCurrentWin(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)нажимает назад в браузере$")
    public void backPage() {
        super.backPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)переходит на страницу \"(.*?)\" по ссылке$")
    public void goToUrl(String url) {
        super.goToUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)(?:переходит на|открывает) url \"(.*?)\"$")
    public void goToPageByUrl(String url) throws PageInitializationException {
        super.goToPageByUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^обновляем страницу$")
    public void reInitPage() {
        super.reInitPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^в фокусе находится элемент \"([^\"]*)\"$")
    public void isElementFocused(String element) {
        super.isElementFocused(element);
    }
}
