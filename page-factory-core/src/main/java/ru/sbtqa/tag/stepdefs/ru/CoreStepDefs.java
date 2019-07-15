package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.Transform;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.junit.CoreSetupSteps;
import ru.sbtqa.tag.pagefactory.junit.CoreSteps;
import ru.sbtqa.tag.pagefactory.transformer.ConditionTransformer;
import ru.sbtqa.tag.pagefactory.transformer.enums.Condition;

import java.util.List;

public class CoreStepDefs {

    private final CoreSteps coreSteps = CoreSteps.getInstance();

    @Before(order = 0)
    public void preSetUp() {
        CoreSetupSteps.preSetUp();
    }

    @Before(order = 99999)
    public void setUp() {
        CoreSetupSteps.setUp();
    }

    @After(order = 1)
    public void tearDown() {
        CoreSetupSteps.tearDown();
    }

    @И("^(?:пользователь |он )?(?:находится на странице|открывается страница|открывается вкладка мастера) \"([^\"]*)\"$")
    public void openPage(String title) throws PageInitializationException {
        coreSteps.openPage(title);
    }

    @И("^(?:пользователь |он )?\\(([^)]*)\\)$")
    public void action(String action) throws NoSuchMethodException {
        coreSteps.action(action);
    }

    @И("^(?:пользователь |он )?\\(([^)]*)\\) (?:с параметром )?\"([^\"]*)\"$")
    public void action(String action, String param) throws NoSuchMethodException {
        coreSteps.action(action, param);
    }

    @И("^(?:пользователь |он )?\\(([^)]*)\\) (?:с параметрами )?\"([^\"]*)\" \"([^\"]*)\"$")
    public void action(String action, String param1, String param2) throws NoSuchMethodException {
        coreSteps.action(action, param1, param2);
    }

    @И("^(?:пользователь |он )?\\(([^)]*)\\) (?:с параметрами )?\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void action(String action, String param1, String param2, String param3) throws NoSuchMethodException {
        coreSteps.action(action, param1, param2, param3);
    }

    @И("^(?:пользователь |он )?\\(([^)]*)\\) данными$")
    public void action(String action, DataTable dataTable) throws NoSuchMethodException {
        coreSteps.action(action, dataTable);
    }

    @И("^(?:пользователь |он )?\\(([^)]*)\\) \"([^\"]*)\" данными$")
    public void action(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        coreSteps.action(action, param, dataTable);
    }

    @И("^(?:пользователь |он )?\\(([^)]*)\\) из списка$")
    public void action(String action, List<String> list) throws NoSuchMethodException {
        coreSteps.action(action, list);
    }

    @И("^(?:пользователь |он )?заполняет поле \"([^\"]*)\"(?: значением)?$")
    @Когда("^(?:пользователь |он )?заполняет поле \"([^\"]*)\" (?:значением )?\"([^\"]*)\"$")
    public void fill(String elementTitle, String text) throws PageException {
        coreSteps.fill(elementTitle, text);
    }

    @И("^(?:пользователь |он )?кликает по ссылке \"([^\"]*)\"$")
    @Когда("^(?:пользователь |он )?нажимает кнопку \"([^\"]*)\"$")
    public void click(String elementTitle) throws PageException {
        coreSteps.click(elementTitle);
    }

    @И("^(?:пользователь |он )?нажимает клавишу \"([^\"]*)\"$")
    public void pressKey(String keyName) {
        coreSteps.pressKey(keyName);
    }

    @И("^(?:пользователь |он )?нажимает клавишу \"([^\"]*)\" на элементе \"([^\"]*)\"$")
    public void pressKey(String keyName, String elementTitle) throws PageException {
        coreSteps.pressKey(keyName, elementTitle);
    }

    @И("^(?:пользователь |он )?выбирает в \"([^\"]*)\" значение \"([^\"]*)\"$")
    public void select(String elementTitle, String option) throws PageException {
        coreSteps.select(elementTitle, option);
    }

    @И("^(?:пользователь |он )?отмечает чекбокс \"([^\"]*)\"$")
    public void setCheckBox(String elementTitle) throws PageException {
        coreSteps.setCheckBox(elementTitle);
    }

    @И("^(?:пользователь |он )?проверяет что в поле \"([^\"]*)\" значение \"([^\"]*)\"$")
    public void checkValueIsEqual(String elementTitle, String text) throws PageException {
        coreSteps.checkValueIsEqual(elementTitle, text);
    }

    @И("^(?:пользователь |он )?проверяет что в поле \"([^\"]*)\" не значение \"([^\"]*)\"$")
    public void checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        coreSteps.checkValueIsNotEqual(elementTitle, text);
    }

    @И("^(?:пользователь |он )?проверяет что поле \"([^\"]*)\" содержит значение \"([^\"]*)\"$")
    public void checkValueContains(String elementTitle, String text) throws PageException {
        coreSteps.checkValueContains(elementTitle, text);
    }

    @И("^(?:пользователь |он )?проверяет что поле \"([^\"]*)\" не содержит значение \"([^\"]*)\"$")
    public void checkValueNotContains(String elementTitle, String text) throws PageException {
        coreSteps.checkValueNotContains(elementTitle, text);
    }

    @И("^(?:пользователь |он )?проверяет что поле \"([^\"]*)\" непустое$")
    public void checkNotEmpty(String elementTitle) throws PageException {
        coreSteps.checkNotEmpty(elementTitle);
    }

    @И("^(?:пользователь |он )?проверяет что поле \"([^\"]*)\" пустое$")
    public void checkEmpty(String elementTitle) throws PageException {
        coreSteps.checkEmpty(elementTitle);
    }

    @И("^элемент \"([^\"]*)\" в фокусе")
    public void isElementFocused(String element) {
        coreSteps.isElementFocused(element);
    }

    @И("^(?:пользователь |он )?вставляет фрагмент \"([^\"]*)\"$")
    @Когда("^(?:пользователь |он |)выполняет сценарий \"([^\"]*)\"$")
    @Тогда("^(?:пользователь |он |)выполняет \"([^\"]*)\"$")
    public void userInsertsFragment(String fragmentName) throws FragmentException {
            coreSteps.userInsertsFragment(fragmentName);
    }

    @Когда("^(?:пользователь |он )?ожидает появления элемента \"([^\"]*)\"$")
    public void appearElement(String elementName) throws PageException {
        coreSteps.appearElement(elementName);
    }

    @Когда("^(?:пользователь |он )?ожидает (\\d+) секунд(?:у)? появления элемента \"([^\"]*)\"$")
    public void appearElement(int timeout, String elementName) throws PageException {
        coreSteps.appearElement(timeout, elementName);
    }

    @Когда("^(?:пользователь |он )?ожидает исчезновения элемента \"([^\"]*)\"$")
    public void waitInvisibility(String elementName) throws PageException {
        coreSteps.waitInvisibility(elementName);
    }

    @Когда("^(?:пользователь |он )?ожидает (\\d+) секунд(?:у)? исчезновения элемента \"([^\"]*)\"$")
    public void waitInvisibility(int timeout, String elementName) throws PageException {
        coreSteps.waitInvisibility(timeout, elementName);
    }

    @Когда("^(?:пользователь |он )?ожидает что значение атрибута \"([^\"]*)\" в элементе \"([^\"]*)\" станет равно \"([^\"]*)\"$")
    public void waitChangeAttribute(String attribute, String elementName, String attributeValue) throws PageException {
        coreSteps.waitChangeAttribute(attribute, elementName, attributeValue);
    }

    @Когда("^(?:пользователь |он )?ожидает (\\d+) секунд(?:у)? что значение атрибута \"([^\"]*)\" в элементе \"([^\"]*)\" станет равно \"([^\"]*)\"$")
    public void waitChangeAttribute(int timeout, String attribute, String elementName, String attributeValue) throws PageException {
        coreSteps.waitChangeAttribute(timeout, attribute, elementName, attributeValue);
    }

    @Когда("^(?:пользователь |он )?ожидает что значение атрибута \"([^\"]*)\" в элементе \"([^\"]*)\" (не )?должно содержать \"([^\"]*)\"$")
    public void waitAttributeContains(String attribute, String elementName,
                                      @Transform(ConditionTransformer.class) Condition negation, String partAttributeValue) throws PageException {
        coreSteps.waitAttributeContains(attribute, elementName, negation, partAttributeValue);
    }

    @Когда("^(?:пользователь |он )?ожидает (\\d+) секунд(?:у)? что значение атрибута \"([^\"]*)\" в элементе \"([^\"]*)\" (не )?должно содержать \"([^\"]*)\"$")
    public void waitAttributeContains(int timeout, String attribute, String elementName,
                                      @Transform(ConditionTransformer.class) Condition negation, String partAttributeValue) throws PageException {
        coreSteps.waitAttributeContains(timeout, attribute, elementName, negation, partAttributeValue);
    }

    @Когда("^(?:пользователь |он )?ожидает что элемент \"([^\"]*)\" (не )?должен содержать текст \"([^\"]*)\"$")
    public void waitElementContainsText(String elementName, @Transform(ConditionTransformer.class) Condition negation, String text) throws PageException {
        coreSteps.waitElementContainsText(elementName, negation, text);
    }

    @Когда("^(?:пользователь |он )?ожидает (\\d+) секунд(?:у)? что элемент \"([^\"]*)\" (не )?должен содержать текст \"([^\"]*)\"$")
    public void waitElementContainsText(int timeout, String elementName, @Transform(ConditionTransformer.class) Condition negation, String text) throws PageException {
        coreSteps.waitElementContainsText(timeout, elementName, negation, text);
    }

    @Когда("^(?:пользователь |он )?ожидает что элемент \"([^\"]*)\" станет кликабельным$")
    public void waitClickability(String elementName) throws PageException {
        coreSteps.waitClickability(elementName);
    }

    @Когда("^(?:пользователь |он )?ожидает (\\d+) секунд(?:у)? что элемент \"([^\"]*)\" станет кликабельным$")
    public void waitClickability(int timeout, String elementName) throws PageException {
        coreSteps.waitClickability(timeout, elementName);
    }

    @And("^(?:пользователь |он )?принимает уведомление с текстом \"([^\"]*)\"$")
    public void acceptAlert(String text) throws WaitException {
        coreSteps.acceptAlert(text);
    }

    @And("^(?:пользователь |он )?отклоняет уведомление с текстом \"([^\"]*)\"$")
    public void dismissAlert(String text) throws WaitException {
        coreSteps.dismissAlert(text);
    }
}
