package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import io.cucumber.datatable.DataTable;
import ru.sbtqa.tag.pagefactory.html.junit.AccordionSteps;
import ru.sbtqa.tag.pagefactory.transformer.ContainCondition;
import ru.sbtqa.tag.pagefactory.transformer.SearchStrategy;

public class AccordionStepDefs {

    private final AccordionSteps accordionSteps = AccordionSteps.getInstance();

    @Когда("^(?:пользователь |он )?открывает аккордеон \"([^\"]*)\"$")
    public void open(String accordionName) {
        accordionSteps.open(accordionName);
    }

    @Когда("^(?:пользователь |он )?закрывает аккордеон \"([^\"]*)\"$")
    public void close(String accordionName) {
        accordionSteps.close(accordionName);
    }

    @Когда("^содержимое аккордеона \"([^\"]*)\" (отображается|не отображается)$")
    public void checkAccordionOpened(String accordionName, ContainCondition condition) {
        accordionSteps.checkAccordionOpened(condition, accordionName);
    }

    @И("^текст содержимого аккордеона \"([^\"]*)\" равен$")
    @Когда("^текст содержимого аккордеона \"([^\"]*)\" равен \"([^\"]*)\"$")
    public void checkContent(String accordionName, String expectedText) {
        accordionSteps.checkContent(accordionName, expectedText, SearchStrategy.EQUALS);
    }

    @И("^текст содержимого аккордеона \"([^\"]*)\" содержит фрагмент$")
    @Когда("^текст содержимого аккордеона \"([^\"]*)\" содержит фрагмент \"([^\"]*)\"$")
    public void checkContentContains(String accordionName, String expectedText) {
        accordionSteps.checkContent(accordionName, expectedText, SearchStrategy.CONTAINS);
    }

    @И("^текст заголовка аккордеона \"([^\"]*)\" равен$")
    @Когда("^текст заголовка аккордеона \"([^\"]*)\" равен \"([^\"]*)\"$")
    public void checkHeader(String accordionName, String expectedText) {
        accordionSteps.checkHeader(accordionName, expectedText, SearchStrategy.EQUALS);
    }

    @И("^текст заголовка аккордеона \"([^\"]*)\" содержит фрагмент$")
    @Когда("^текст заголовка аккордеона \"([^\"]*)\" содержит фрагмент \"([^\"]*)\"$")
    public void checkHeaderContains(String accordionName, String expectedText) {
        accordionSteps.checkHeader(accordionName, expectedText, SearchStrategy.CONTAINS);
    }

    @И("^текст элемента \"([^\"]*)\" в заголовке аккордеона \"([^\"]*)\" строго равен$")
    @Когда("^текст элемента \"([^\"]*)\" в заголовке аккордеона \"([^\"]*)\" строго равен \"([^\"]*)\"$")
    public void checkHeaderElement(String elementName, String accordionName, String expectedText) {
        accordionSteps.checkHeaderElement(elementName, accordionName, expectedText);
    }

    @И("^текст элемента \"([^\"]*)\" в содержимом аккордеона \"([^\"]*)\" строго равен$")
    @Когда("^текст элемента \"([^\"]*)\" в содержимом аккордеона \"([^\"]*)\" строго равен \"([^\"]*)\"$")
    public void checkContentElement(String elementName, String accordionName, String expectedText) {
        accordionSteps.checkContentElement(elementName, accordionName, expectedText);
    }

    @Когда("^проверяет текст элементов в содержимом аккордеона \"([^\"]*)\"$")
    public void checkElements(String accordionName, DataTable expectedTable) {
        accordionSteps.checkElements(accordionName, expectedTable);
    }
}
