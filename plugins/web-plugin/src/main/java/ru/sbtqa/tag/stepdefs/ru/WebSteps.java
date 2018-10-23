package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.ru.И;
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
    @И("^открывается копия страницы в новой вкладке$")
    public void openCopyPage() {
        super.openCopyPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?переключается на соседнюю вкладку$")
    public void switchesToNextTab() {
        super.switchesToNextTab();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^URL соответствует \"([^\"]*)\"$")
    public void urlMatches(String url) {
        super.urlMatches(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?закрывает текущее окно и возвращается на \"([^\"]*)\"$")
    public void closingCurrentWin(String title) {
        super.closingCurrentWin(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?нажимает назад в браузере$")
    public void backPage() {
        super.backPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?переходит на страницу \"([^\"]*)\" по ссылке$")
    public void goToUrl(String url) {
        super.goToUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?(?:переходит на|открывает) url \"([^\"]*)\"$")
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
    @And("^(?:пользователь |он )?принимает уведомление с текстом \"([^\"]*)\"$")
    public void acceptAlert(String text) throws WaitException {
        super.acceptAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?отклоняет уведомление с текстом \"([^\"]*)\"$")
    public void dismissAlert(String text) throws WaitException {
        super.dismissAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что текст \"([^\"]*)\" появляется на странице$")
    public void checkTextAppears(String text) throws WaitException {
        super.checkTextAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что текст \"([^\"]*)\" отсутствует на странице$")
    public void checkTextIsNotPresent(String text) {
        super.checkTextIsNotPresent(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что модальное окно с текстом \"([^\"]*)\" появляется$")
    public void checkModalWindowAppears(String text) throws WaitException {
        super.checkModalWindowAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что элемент с текстом \"([^\"]*)\" существует$")
    @When("^(?:пользователь |он )?проверяет что текст \"([^\"]*)\" отоброжается$")
    public void checkElementWithTextIsPresent(String text) {
        super.checkElementWithTextIsPresent(text);
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
