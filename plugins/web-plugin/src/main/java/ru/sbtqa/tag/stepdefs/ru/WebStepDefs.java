package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.ru.И;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.stepdefs.WebSetupSteps;
import ru.sbtqa.tag.stepdefs.WebGenericSteps;

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
    @И("^открывается копия страницы в новой вкладке$")
    public WebStepDefs openCopyPage() {
        return super.openCopyPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?переключается на соседнюю вкладку$")
    public WebStepDefs switchesToNextTab() {
        return super.switchesToNextTab();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^URL соответствует \"([^\"]*)\"$")
    public WebStepDefs urlMatches(String url) {
        return super.urlMatches(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?закрывает текущее окно и возвращается на \"([^\"]*)\"$")
    public WebStepDefs closingCurrentWin(String title) {
        return super.closingCurrentWin(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?нажимает назад в браузере$")
    public WebStepDefs backPage() {
        return super.backPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?переходит на страницу \"([^\"]*)\" по ссылке$")
    public WebStepDefs goToUrl(String url) {
        return super.goToUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?(?:переходит на|открывает) url \"([^\"]*)\"$")
    public WebStepDefs goToPageByUrl(String url) throws PageInitializationException {
        return super.goToPageByUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^обновляем страницу$")
    public WebStepDefs reInitPage() {
        return super.reInitPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?принимает уведомление с текстом \"([^\"]*)\"$")
    public WebStepDefs acceptAlert(String text) throws WaitException {
        return super.acceptAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?отклоняет уведомление с текстом \"([^\"]*)\"$")
    public WebStepDefs dismissAlert(String text) throws WaitException {
        return super.dismissAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что текст \"([^\"]*)\" появляется на странице$")
    public WebStepDefs checkTextAppears(String text) throws WaitException {
        return super.checkTextAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что текст \"([^\"]*)\" отсутствует на странице$")
    public WebStepDefs checkTextIsNotPresent(String text) {
        return super.checkTextIsNotPresent(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что модальное окно с текстом \"([^\"]*)\" появляется$")
    public WebStepDefs checkModalWindowAppears(String text) throws WaitException {
        return super.checkModalWindowAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что элемент с текстом \"([^\"]*)\" существует$")
    @When("^(?:пользователь |он )?проверяет что текст \"([^\"]*)\" отоброжается$")
    public WebStepDefs checkElementWithTextIsPresent(String text) {
        return super.checkElementWithTextIsPresent(text);
    }
}
