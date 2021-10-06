package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import io.cucumber.datatable.DataTable;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.html.junit.HtmlSetupSteps;
import ru.sbtqa.tag.pagefactory.html.junit.HtmlSteps;
import ru.sbtqa.tag.pagefactory.transformer.ContainCondition;
import ru.sbtqa.tag.pagefactory.transformer.Presence;

import java.util.List;

public class HtmlStepDefs {

    private final HtmlSteps htmlSteps = HtmlSteps.getInstance();

    @Before(order = 2)
    public void initHtml() {
        HtmlSetupSteps.initHtml();
    }

    @After(order = 2)
    public void tearDown() {
        HtmlSetupSteps.tearDown();
    }
    
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\)$")
    public void actionInBlock(String block, String action) {
        htmlSteps.actionInBlock(block, action);
    }

    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметрами из таблицы$")
    public void actionInBlock(String block, String action, DataTable dataTable) {
        htmlSteps.actionInBlock(block, action, dataTable);
    }

    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметром \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param) {
        htmlSteps.actionInBlock(block, action, param);
    }

    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметрами \"([^\"]*)\" \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param1, String param2) {
        htmlSteps.actionInBlock(block, action, param1, param2);
    }

    @И("^(?:пользователь |он )?в списке \"([^\"]*)\" находит элемент со значением \"([^\"]*)\"$")
    public void find(String listTitle, String value) throws PageException {
        htmlSteps.find(listTitle, value);
    }

    @Когда("^(?:пользователь |он )?заполняет форму$")
    @И("^(?:пользователь |он )?отмечает признаки$")
    public void fillForm(DataTable dataTable) {
        htmlSteps.fillForm(dataTable);
    }

    @Когда("^элемент обязательно (при|от)сутствует на странице \"([^\"]*)\"$")
    @И("^(присутствует|не отображается|отображается) элемент \"([^\"]*)\"$")
    public void elementAlwaysPresent(Presence present, String elementName) {
        htmlSteps.elementAlwaysPresent(present, elementName);
    }

    @Когда("^элементы обязательно (при|от)сутствуют на странице$")
    @И("^(присутствуют|не отображаются|отображаются) элементы$")
    public void elementsAlwaysPresent(Presence present, List<String> elementNames) {
        htmlSteps.elementsAlwaysPresent(present, elementNames);
    }

    @Когда("^элементы (доступны|не доступны) для редактирования$")
    @И("^присутствуют (активные|не активные) элементы$")
    public void elementsPresentAndDisabled(ContainCondition condition, List<String> elementNames) {
        htmlSteps.elementsPresentAndDisabled(condition, elementNames);
    }

    @Когда("^элемент \"([^\"]*)\" (доступен|не доступен) для редактирования$")
    @И("^элемент \"([^\"]*)\" (активен|не активен)$")
    public void elementPresentAndDisabled(String elementName, ContainCondition condition) {
        htmlSteps.elementPresentAndDisabled(elementName, condition);
    }

    @Когда("^(выбрана|не выбрана) кнопка \"([^\"]*)\"$")
    public void elementsPresentAndSelected(ContainCondition condition, String elementName) {
        htmlSteps.elementsPresentAndSelected(condition, elementName);
    }

    @Когда("^запоминает значение элемента \"([^\"]*)\" в переменную \"([^\"]*)\"$")
    public void putInStash(String elementName, String variableName) {
        htmlSteps.putInStash(elementName, variableName);
    }

    @Когда("^наводит на элемент \"([^\"]*)\"$")
    public void moveToElement(String elementName) {
        htmlSteps.moveToElement(elementName);
    }

    @Когда("^отображается список текстовых значений:?$")
    public void checkTextList(List<String> textList) {
        htmlSteps.checkTextList(textList);
    }

    @И("^отображается текст$")
    @Когда("^отображается текст \"([^\"]*)\"$")
    public void checkText(String expectedText) {
        htmlSteps.checkText(expectedText);
    }

    @Когда("^открывается страница \"([^\"]*)\" и ожидает элемент \"([^\"]*)\"$")
    public void openPage(String pageName, String elementName) throws PageInitializationException {
        htmlSteps.openPage(pageName, elementName);
    }

    @Когда("^проверяет форму$")
    public void checkForm(DataTable dataTable) {
        htmlSteps.checkForm(dataTable);
    }

    @И("^текст элемента \"([^\"]*)\" содержит$")
    @Когда("^текст элемента \"([^\"]*)\" содержит \"([^\"]*)\"$")
    public void checkElementText(String elementName, String expectedText) {
        htmlSteps.checkElementText(elementName, expectedText);
    }

    @И("^текст элемента \"([^\"]*)\" строго равен$")
    @Когда("^текст элемента \"([^\"]*)\" строго равен \"([^\"]*)\"$")
    public void checkTextEquals(String elementName, String expectedText) {
        htmlSteps.checkTextEquals(elementName, expectedText);
    }

    @Когда("^текст элемента \"([^\"]*)\" соответствует маске \"([^\"]*)\"$")
    public void checkInputMask(String elementName, String mask) {
        htmlSteps.checkInputMask(elementName, mask);
    }

    @Когда("^(?:пользователь |он )?кликает по перекрытому элементу \"([^\"]*)\"$")
    public void clickByOverlapElement(String elementName) {
        htmlSteps.clickByOverlapElement(elementName);
    }
}


