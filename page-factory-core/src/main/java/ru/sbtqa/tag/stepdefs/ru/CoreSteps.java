package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.ru.И;
import java.util.List;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.stepdefs.CoreGenericSteps;

public class CoreSteps extends CoreGenericSteps {

    @Override
    @Before(order = 0)
    public void preSetUp(Scenario scenario) {
        super.preSetUp(scenario);
    }

    @Override
    @Before(order = 99999)
    public void setUp(Scenario scenario) {
        super.setUp(scenario);
    }

    @Override
    @After(order = 99999)
    public void tearDown() {
        super.tearDown();
    }

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
    public void userActionNoParams(String action) throws NoSuchMethodException {
        super.userActionNoParams(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\) (?:с параметром |)\"([^\"]*)\"$")
    public void userActionOneParam(String action, String param) throws NoSuchMethodException {
        super.userActionOneParam(action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\) (?:с параметрами |)\"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionTwoParams(String action, String param1, String param2) throws NoSuchMethodException {
        super.userActionTwoParams(action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\) (?:с параметрами |)\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionThreeParams(String action, String param1, String param2, String param3) throws NoSuchMethodException {
        super.userActionThreeParams(action, param1, param2, param3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\) данными$")
    public void userActionTableParam(String action, DataTable dataTable) throws NoSuchMethodException {
        super.userActionTableParam(action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\) [^\"]*\"([^\"]*)\" данными$")
    public void userDoActionWithObject(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        super.userDoActionWithObject(action, param, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)\\((.*?)\\) из списка$")
    public void userActionListParam(String action, List<String> list) throws NoSuchMethodException {
        super.userActionListParam(action, list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^пользователь заполняет поле \"(.*?)\" (?:значением |)\"(.*?)\"$")
    public void fill(String elementTitle, String text) throws PageException {
        super.fill(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^пользователь кликает по ссылке \"(.*?)\"$")
    @When("^пользователь нажимает кнопку \"(.*?)\"$")
    public void click(String elementTitle) throws PageException {
        super.click(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^пользователь нажимает клавишу \"(.*?)\"$")
    public void pressKey(String keyName) {
        super.pressKey(keyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^пользователь нажимает клавишу \"(.*?)\" на элементе \"(.*?)\"$")
    public void pressKey(String keyName, String elementTitle) throws PageException {
        super.pressKey(keyName, elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^пользователь выбирает в \"(.*?)\" значение \"(.*?)\"$")
    public void select(String elementTitle, String option) throws PageException {
        super.select(elementTitle, option);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^пользователь отмечает чекбокс \"(.*?)\"$")
    public void setCheckBox(String elementTitle) throws PageException {
        super.setCheckBox(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^пользователь проверяет что в поле \"(.*?)\" значение \"(.*?)\"$")
    public void checkValueIsEqual(String elementTitle, String text) throws PageException {
        super.checkValueIsEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^пользователь проверяет что в поле \"(.*?)\" не значение \"(.*?)\"$")
    public void checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        super.checkValueIsNotEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^пользователь проверяет что поле \"(.*?)\" непустое$")
    public void checkNotEmpty(String elementTitle) throws PageException {
        super.checkNotEmpty(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^пользователь проверяет что поле \"(.*?)\" пустое$")
    public void checkEmpty(String elementTitle) throws PageException {
        super.checkEmpty(elementTitle);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он |)вставляет фрагмент \"([^\"]*)\"$")
    public void userInsertsFragment(String fragmentName) throws FragmentException {
        super.userInsertsFragment(fragmentName);
    }
}
