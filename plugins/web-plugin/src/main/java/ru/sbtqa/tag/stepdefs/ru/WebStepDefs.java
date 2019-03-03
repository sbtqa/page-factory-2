package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.ru.И;
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

    @И("^открывается копия страницы в новой вкладке$")
    public void openCopyPage() {
        webSteps.openCopyPage();
    }

    @И("^(?:пользователь |он )?переключается на соседнюю вкладку$")
    public void switchesToNextTab() {
        webSteps.switchesToNextTab();
    }

    @И("^URL соответствует \"([^\"]*)\"$")
    public void urlMatches(String url) {
        webSteps.urlMatches(url);
    }

    @И("^(?:пользователь |он )?закрывает текущее окно и возвращается на \"([^\"]*)\"$")
    public void closingCurrentWin(String title) {
        webSteps.closingCurrentWin(title);
    }

    @И("^(?:пользователь |он )?нажимает назад в браузере$")
    public void backPage() {
        webSteps.backPage();
    }

    @И("^(?:пользователь |он )?(?:переходит на|открывает) url \"([^\"]*)\"$")
    public void goToUrl(String url) {
        webSteps.goToUrl(url);
    }

    @И("^обновляем страницу$")
    public void reInitPage() {
        webSteps.reInitPage();
    }

    @And("^(?:пользователь |он )?принимает уведомление с текстом \"([^\"]*)\"$")
    public void acceptAlert(String text) throws WaitException {
        webSteps.acceptAlert(text);
    }

    @And("^(?:пользователь |он )?отклоняет уведомление с текстом \"([^\"]*)\"$")
    public void dismissAlert(String text) throws WaitException {
        webSteps.dismissAlert(text);
    }

    @And("^(?:пользователь |он )?проверяет что текст \"([^\"]*)\" появляется на странице$")
    public void checkTextAppears(String text) throws WaitException, InterruptedException {
        webSteps.checkTextAppears(text);
    }

    @And("^(?:пользователь |он )?проверяет что текст \"([^\"]*)\" отсутствует на странице$")
    public void checkTextIsNotPresent(String text) throws InterruptedException {
        webSteps.checkTextIsNotPresent(text);
    }

    @And("^(?:пользователь |он )?проверяет что модальное окно с текстом \"([^\"]*)\" появляется$")
    public void checkModalWindowAppears(String text) throws WaitException {
        webSteps.checkModalWindowAppears(text);
    }

    @And("^(?:пользователь |он )?проверяет что элемент с текстом \"([^\"]*)\" существует$")
    @When("^(?:пользователь |он )?проверяет что текст \"([^\"]*)\" отоброжается$")
    public void checkElementWithTextIsPresent(String text) {
        webSteps.checkElementWithTextIsPresent(text);
    }
}
