package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.ru.И;
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
    @И("^открывается копия страницы в новой вкладке$")
    public WebSteps openCopyPage() {
        return super.openCopyPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?переключается на соседнюю вкладку$")
    public WebSteps switchesToNextTab() {
        return super.switchesToNextTab();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^URL соответствует \"([^\"]*)\"$")
    public WebSteps urlMatches(String url) {
        return super.urlMatches(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?закрывает текущее окно и возвращается на \"([^\"]*)\"$")
    public WebSteps closingCurrentWin(String title) {
        return super.closingCurrentWin(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?нажимает назад в браузере$")
    public WebSteps backPage() {
        return super.backPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?переходит на страницу \"([^\"]*)\" по ссылке$")
    public WebSteps goToUrl(String url) {
        return super.goToUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?(?:переходит на|открывает) url \"([^\"]*)\"$")
    public WebSteps goToPageByUrl(String url) throws PageInitializationException {
        return super.goToPageByUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^обновляем страницу$")
    public WebSteps reInitPage() {
        return super.reInitPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?принимает уведомление с текстом \"([^\"]*)\"$")
    public WebSteps acceptAlert(String text) throws WaitException {
        return super.acceptAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?отклоняет уведомление с текстом \"([^\"]*)\"$")
    public WebSteps dismissAlert(String text) throws WaitException {
        return super.dismissAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что текст \"([^\"]*)\" появляется на странице$")
    public WebSteps checkTextAppears(String text) throws WaitException {
        return super.checkTextAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что текст \"([^\"]*)\" отсутствует на странице$")
    public WebSteps checkTextIsNotPresent(String text) {
        return super.checkTextIsNotPresent(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что модальное окно с текстом \"([^\"]*)\" появляется$")
    public WebSteps checkModalWindowAppears(String text) throws WaitException {
        return super.checkModalWindowAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что элемент с текстом \"([^\"]*)\" существует$")
    @When("^(?:пользователь |он )?проверяет что текст \"([^\"]*)\" отоброжается$")
    public WebSteps checkElementWithTextIsPresent(String text) {
        return super.checkElementWithTextIsPresent(text);
    }
}
