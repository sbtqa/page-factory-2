package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.Transform;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import java.util.List;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.transformer.ConditionTransformer;
import ru.sbtqa.tag.pagefactory.transformer.enums.Condition;
import ru.sbtqa.tag.stepdefs.CoreGenericSteps;
import ru.sbtqa.tag.stepdefs.CoreSetupSteps;

public class CoreStepDefs extends CoreGenericSteps<CoreStepDefs> {

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
    @И("^(?:пользователь |он )?заполняет поле \"([^\"]*)\"(?: значением)?$")
    @Когда("^(?:пользователь |он )?заполняет поле \"([^\"]*)\" (?:значением )?\"([^\"]*)\"$")
    public CoreStepDefs fill(String elementTitle, String text) throws PageException {
        return super.fill(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?кликает по ссылке \"([^\"]*)\"$")
    @Когда("^(?:пользователь |он )?нажимает кнопку \"([^\"]*)\"$")
    public CoreStepDefs click(String elementTitle) throws PageException {
        return super.click(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?нажимает клавишу \"([^\"]*)\"$")
    public CoreStepDefs pressKey(String keyName) {
        return super.pressKey(keyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?нажимает клавишу \"([^\"]*)\" на элементе \"([^\"]*)\"$")
    public CoreStepDefs pressKey(String keyName, String elementTitle) throws PageException {
        return super.pressKey(keyName, elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?выбирает в \"([^\"]*)\" значение \"([^\"]*)\"$")
    public CoreStepDefs select(String elementTitle, String option) throws PageException {
        return super.select(elementTitle, option);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?отмечает чекбокс \"([^\"]*)\"$")
    public CoreStepDefs setCheckBox(String elementTitle) throws PageException {
        return super.setCheckBox(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?проверяет что в поле \"([^\"]*)\" значение \"([^\"]*)\"$")
    public CoreStepDefs checkValueIsEqual(String elementTitle, String text) throws PageException {
        return super.checkValueIsEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?проверяет что в поле \"([^\"]*)\" не значение \"([^\"]*)\"$")
    public CoreStepDefs checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        return super.checkValueIsNotEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?проверяет что поле \"([^\"]*)\" непустое$")
    public CoreStepDefs checkNotEmpty(String elementTitle) throws PageException {
        return super.checkNotEmpty(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?проверяет что поле \"([^\"]*)\" пустое$")
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

    @Override
    @Когда("^(?:пользователь |он )?ожидает появления элемента \"([^\"]*)\"$")
    public CoreStepDefs appearElement(String elementName) throws PageException {
        return super.appearElement(elementName);
    }
    
    @Override
    @Когда("^(?:пользователь |он )?ожидает (\\d+) секунд(?:у)? появления элемента \"([^\"]*)\"$")
    public CoreStepDefs appearElement(int timeout, String elementName) throws PageException {
        return super.appearElement(timeout, elementName);
    }

    @Override
    @Когда("^(?:пользователь |он )?ожидает исчезновения элемента \"([^\"]*)\"$")
    public CoreStepDefs waitInvisibility(String elementName) throws PageException {
        return super.waitInvisibility(elementName);
    }

    @Override
    @Когда("^(?:пользователь |он )?ожидает (\\d+) секунд(?:у)? исчезновения элемента \"([^\"]*)\"$")
    public CoreStepDefs waitInvisibility(int timeout, String elementName) throws PageException {
        return super.waitInvisibility(timeout, elementName);
    }

    @Override
    @Когда("^(?:пользователь |он )?ожидает что значение атрибута \"([^\"]*)\" в элементе \"([^\"]*)\" станет равно \"([^\"]*)\"$")
    public CoreStepDefs waitChangeAttribute(String attribute, String elementName, String attributeValue) throws PageException {
        return super.waitChangeAttribute(attribute, elementName, attributeValue);
    }

    @Override
    @Когда("^(?:пользователь |он )?ожидает (\\d+) секунд(?:у)? что значение атрибута \"([^\"]*)\" в элементе \"([^\"]*)\" станет равно \"([^\"]*)\"$")
    public CoreStepDefs waitChangeAttribute(int timeout, String attribute, String elementName, String attributeValue) throws PageException {
        return super.waitChangeAttribute(timeout, attribute, elementName, attributeValue);
    }

    @Override
    @Когда("^(?:пользователь |он )?ожидает что значение атрибута \"([^\"]*)\" в элементе \"([^\"]*)\" (не )?должно содержать \"([^\"]*)\"$")
    public CoreStepDefs waitAttributeContains(String attribute, String elementName,
            @Transform(ConditionTransformer.class) Condition negation, String partAttributeValue) throws PageException {
        return super.waitAttributeContains(attribute, elementName, negation, partAttributeValue);
    }

    @Override
    @Когда("^(?:пользователь |он )?ожидает (\\d+) секунд(?:у)? что значение атрибута \"([^\"]*)\" в элементе \"([^\"]*)\" (не )?должно содержать \"([^\"]*)\"$")
    public CoreStepDefs waitAttributeContains(int timeout, String attribute, String elementName,
            @Transform(ConditionTransformer.class) Condition negation, String partAttributeValue) throws PageException {
        return super.waitAttributeContains(timeout, attribute, elementName, negation, partAttributeValue);
    }

    @Override
    @Когда("^(?:пользователь |он )?ожидает что элемент \"([^\"]*)\" (не )?должен содержать текст \"([^\"]*)\"$")
    public CoreStepDefs waitElementContainsText(String elementName, @Transform(ConditionTransformer.class) Condition negation, String text) throws PageException {
        return super.waitElementContainsText(elementName, negation, text);
    }

    @Override
    @Когда("^(?:пользователь |он )?ожидает (\\d+) секунд(?:у)? что элемент \"([^\"]*)\" (не )?должен содержать текст \"([^\"]*)\"$")
    public CoreStepDefs waitElementContainsText(int timeout, String elementName, @Transform(ConditionTransformer.class) Condition negation, String text) throws PageException {
        return super.waitElementContainsText(timeout, elementName, negation, text);
    }
    
    @Override
    @Когда("^(?:пользователь |он )?ожидает что элемент \"([^\"]*)\" станет кликабельным$")
    public CoreStepDefs waitClickability(String elementName) throws PageException {
        return super.waitClickability(elementName);
    }

    @Override
    @Когда("^(?:пользователь |он )?ожидает (\\d+) секунд(?:у)? что элемент \"([^\"]*)\" станет кликабельным$")
    public CoreStepDefs waitClickability(int timeout, String elementName) throws PageException {
        return super.waitClickability(timeout, elementName);
    }
}
