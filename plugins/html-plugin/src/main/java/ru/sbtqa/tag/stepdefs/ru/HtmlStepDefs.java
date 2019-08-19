package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.Transform;
import cucumber.api.java.Before;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import java.util.List;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.html.junit.HtmlSetupSteps;
import ru.sbtqa.tag.pagefactory.html.junit.HtmlSteps;
import ru.sbtqa.tag.pagefactory.transformer.ConditionTransformer;
import ru.sbtqa.tag.pagefactory.transformer.PresenceTransformer;
import ru.sbtqa.tag.pagefactory.transformer.enums.Condition;
import ru.sbtqa.tag.pagefactory.transformer.enums.Presence;

public class HtmlStepDefs {

    private final HtmlSteps htmlSteps = HtmlSteps.getInstance();

    @Before(order = 2)
    public void initHtml() {
        HtmlSetupSteps.initHtml();
    }
    
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\)$")
    public void actionInBlock(String block, String action) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action);
    }

    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметрами из таблицы$")
    public void actionInBlock(String block, String action, DataTable dataTable) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action, dataTable);
    }

    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметром \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action, param);
    }

    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметрами \"([^\"]*)\" \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param1, String param2) throws NoSuchMethodException {
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
    @И("^(присутствует|не отображается) элемент \"([^\"]*)\"$")
    public void elementAlwaysPresent(@Transform(PresenceTransformer.class) Presence present, String elementName) {
        htmlSteps.elementAlwaysPresent(present, elementName);
    }

    @Когда("^элементы обязательно (при|от)сутствуют на странице$")
    @И("^(присутствуют|не отображаются) элементы$")
    public void elementsAlwaysPresent(@Transform(PresenceTransformer.class) Presence present, List<String> elementNames) {
        htmlSteps.elementsAlwaysPresent(present, elementNames);
    }

    @Когда("^элементы (не)?доступны для редактирования$")
    @И("^присутствуют (не)?активные элементы$")
    public void elementsPresentAndDisabled(@Transform(ConditionTransformer.class) Condition condition, List<String> elementNames) {
        htmlSteps.elementsPresentAndDisabled(condition, elementNames);
    }

    @Когда("^элемент \"([^\"]*)\" (не)?доступен для редактирования$")
    @И("^элемент \"([^\"]*)\" (не)?активен$")
    public void elementPresentAndDisabled(String elementName, @Transform(ConditionTransformer.class) Condition negation) {
        htmlSteps.elementPresentAndDisabled(elementName, negation);
    }

    @Когда("^(не )?выбрана кнопка \"([^\"]*)\"$")
    public void elementsPresentAndSelected(@Transform(ConditionTransformer.class) Condition condition, String elementName) {
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


