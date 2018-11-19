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
import ru.sbtqa.tag.stepdefs.CoreSetupSteps;
import ru.sbtqa.tag.stepdefs.CoreGenericSteps;

public class CoreStepDefs extends CoreGenericSteps<CoreStepDefs> {

    @Before(order = 0)
    public void preSetUp() {
        CoreSetupSteps.preSetUp();
    }

    @Before(order = 99999)
    public void setUp(Scenario scenario) {
        CoreSetupSteps.setUp(scenario);
    }

    @After(order = 1)
    public void tearDown() {
        CoreSetupSteps.tearDown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?(?:находится на странице|открывается страница|открывается вкладка мастера) \"([^\"]*)\"$")
    public CoreStepDefs openPage(String title) throws PageInitializationException {
        return super.openPage(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?\\(([^)]*)\\)$")
    public CoreStepDefs action(String action) throws NoSuchMethodException {
        return super.action(action);
    }

    /**
     * {@inheritDoc}
     */
    @И("^(?:пользователь |он )?\\(([^)]*)\\) (?:с параметром )?\"([^\"]*)\"$")
    public CoreStepDefs action(String action, String param) throws NoSuchMethodException {
        return super.action(action, param);
    }

    /**
     * {@inheritDoc}
     */
    @И("^(?:пользователь |он )?\\(([^)]*)\\) (?:с параметрами )?\"([^\"]*)\" \"([^\"]*)\"$")
    public CoreStepDefs action(String action, String param1, String param2) throws NoSuchMethodException {
        return super.action(action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @И("^(?:пользователь |он )?\\(([^)]*)\\) (?:с параметрами )?\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public CoreStepDefs action(String action, String param1, String param2, String param3) throws NoSuchMethodException {
        return super.action(action, param1, param2, param3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?\\(([^)]*)\\) данными$")
    public CoreStepDefs action(String action, DataTable dataTable) throws NoSuchMethodException {
        return super.action(action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?\\(([^)]*)\\) \"([^\"]*)\" данными$")
    public CoreStepDefs action(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        return super.action(action, param, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?\\(([^)]*)\\) из списка$")
    public CoreStepDefs action(String action, List<String> list) throws NoSuchMethodException {
        return super.action(action, list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?заполняет поле \"([^\"]*)\" (?:значением )?\"([^\"]*)\"$")
    public CoreStepDefs fill(String elementTitle, String text) throws PageException {
        return super.fill(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?кликает по ссылке \"([^\"]*)\"$")
    @When("^(?:пользователь |он )?нажимает кнопку \"([^\"]*)\"$")
    public CoreStepDefs click(String elementTitle) throws PageException {
        return super.click(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?нажимает клавишу \"([^\"]*)\"$")
    public CoreStepDefs pressKey(String keyName) {
        return super.pressKey(keyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?нажимает клавишу \"([^\"]*)\" на элементе \"([^\"]*)\"$")
    public CoreStepDefs pressKey(String keyName, String elementTitle) throws PageException {
        return super.pressKey(keyName, elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?выбирает в \"([^\"]*)\" значение \"([^\"]*)\"$")
    public CoreStepDefs select(String elementTitle, String option) throws PageException {
        return super.select(elementTitle, option);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?отмечает чекбокс \"([^\"]*)\"$")
    public CoreStepDefs setCheckBox(String elementTitle) throws PageException {
        return super.setCheckBox(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что в поле \"([^\"]*)\" значение \"([^\"]*)\"$")
    public CoreStepDefs checkValueIsEqual(String elementTitle, String text) throws PageException {
        return super.checkValueIsEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что в поле \"([^\"]*)\" не значение \"([^\"]*)\"$")
    public CoreStepDefs checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        return super.checkValueIsNotEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что поле \"([^\"]*)\" непустое$")
    public CoreStepDefs checkNotEmpty(String elementTitle) throws PageException {
        return super.checkNotEmpty(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?проверяет что поле \"([^\"]*)\" пустое$")
    public CoreStepDefs checkEmpty(String elementTitle) throws PageException {
        return super.checkEmpty(elementTitle);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?вставляет фрагмент \"([^\"]*)\"$")
    public CoreStepDefs userInsertsFragment(String fragmentName) throws FragmentException {
        return super.userInsertsFragment(fragmentName);
    }
}
