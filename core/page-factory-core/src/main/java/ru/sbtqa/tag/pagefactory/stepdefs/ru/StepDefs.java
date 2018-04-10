package ru.sbtqa.tag.pagefactory.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.java.ru.И;
import java.util.List;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.stepdefs.GenericStepDefs;

public class StepDefs extends GenericStepDefs {

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)(?:находится на странице|открывается страница|открывается вкладка мастера) \"([^\"]*)\"$")
    public void openPage(String title) throws PageInitializationException {
        super.openPage(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\)$")
    public void userActionNoParams(String action) throws PageInitializationException, NoSuchMethodException {
        super.userActionNoParams(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\) (?:с параметром |)\"([^\"]*)\"$")
    public void userActionOneParam(String action, String param) throws PageInitializationException, NoSuchMethodException {
        super.userActionOneParam(action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\) (?:с параметрами |)\"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionTwoParams(String action, String param1, String param2) throws PageInitializationException, NoSuchMethodException {
        super.userActionTwoParams(action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\) (?:с параметрами |)\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionThreeParams(String action, String param1, String param2, String param3) throws PageInitializationException, NoSuchMethodException {
        super.userActionThreeParams(action, param1, param2, param3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\) данными$")
    public void userActionTableParam(String action, DataTable dataTable) throws PageInitializationException, NoSuchMethodException {
        super.userActionTableParam(action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\) [^\"]*\"([^\"]*)\" данными$")
    public void userDoActionWithObject(String action, String param, DataTable dataTable) throws PageInitializationException, NoSuchMethodException {
        super.userDoActionWithObject(action, param, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\) из списка$")
    public void userActionListParam(String action, List<String> list) throws PageInitializationException, NoSuchMethodException {
        super.userActionListParam(action, list);
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
