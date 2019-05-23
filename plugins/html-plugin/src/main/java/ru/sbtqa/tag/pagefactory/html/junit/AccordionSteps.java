package ru.sbtqa.tag.pagefactory.html.junit;

import static ru.sbtqa.tag.pagefactory.web.utils.ElementUtils.checkText;

import cucumber.api.DataTable;
import java.util.Map;
import org.junit.Assert;
import ru.sbtqa.tag.pagefactory.elements.BaseElement;
import ru.sbtqa.tag.pagefactory.elements.accordion.Accordion;
import ru.sbtqa.tag.pagefactory.transformer.enums.Condition;
import ru.sbtqa.tag.pagefactory.transformer.enums.SearchStrategy;
import static java.lang.ThreadLocal.withInitial;

public class AccordionSteps implements Steps {

    static final ThreadLocal<AccordionSteps> storage = withInitial(AccordionSteps::new);

    public static AccordionSteps getInstance() {
        return storage.get();
    }

    /**
     * Opens the accordion by pressing the button
     *
     * @param accordionName accordion name
     * @return Returns itself
     */
    public AccordionSteps open(String accordionName) {
        getElement(accordionName).open();
        return this;
    }

    /**
     * Closes the accordion by pressing the button
     *
     * @param accordionName accordion name
     * @return Returns itself
     */
    public AccordionSteps close(String accordionName) {
        getElement(accordionName).close();
        return this;
    }

    /**
     * Checks whether the accordion is open or close
     *
     * @param condition if {@code Condition.POSITIVE}, checks that the accordion is open.
     * If {@code Condition.NEGATIVE}, checks that the accordion is close
     * @param accordionName accordion name
     * @return Returns itself
     */
    public AccordionSteps checkAccordionOpened(Condition condition, String accordionName) {
        boolean isPositive = condition.equals(Condition.POSITIVE);
        boolean isOpen = getElement(accordionName).isOpen();
        Assert.assertEquals("Accordion is " + (isPositive ? "not " : "") + "open", isPositive, isOpen);
        return this;
    }

    /**
     * Checks the accordion content text
     *
     * @param accordionName accordion name
     * @param expectedText expected accordion content text
     * @param strategy when {@code SearchStrategy.EQUALS}, checks that the accordion text is strictly equal
     * to the expected. When {@code SearchStrategy.CONTAINS}, checks that the accordion text is partially equal
     * to the expected
     * @return Returns itself
     */
    public AccordionSteps checkContent(String accordionName, String expectedText, SearchStrategy strategy) {
        String actualText = getElement(accordionName).getContent().getText();
        checkText(actualText, expectedText, strategy);
        return this;
    }

    /**
     * Checks the accordion header text
     *
     * @param accordionName accordion name
     * @param expectedText expected accordion header text
     * @param strategy when {@code SearchStrategy.EQUALS}, checks that the accordion text is strictly equal
     * to the expected. When {@code SearchStrategy.CONTAINS}, checks that the accordion text is partially equal
     * to the expected
     * @return Returns itself
     */
    public AccordionSteps checkHeader(String accordionName, String expectedText, SearchStrategy strategy) {
        String actualText = getElement(accordionName).getHeader().getText();
        checkText(actualText, expectedText, strategy);
        return this;
    }

    /**
     * For complex accordion.
     * If there are element within the complex element of an accordion header,
     * their text content can be checked by this method
     *
     * @param elementName name of element inside the accordion header
     * @param accordionName accordion name
     * @param expectedText expected accordion header text
     * @return Returns itself
     */
    public AccordionSteps checkHeaderElement(String elementName, String accordionName, String expectedText) {
        String actualText = getElement(accordionName).getHeader().getElement(elementName).getText();
        checkText(actualText, expectedText, SearchStrategy.EQUALS);
        return this;
    }

    /**
     * For complex accordion.
     * If there are element within the complex element of an accordion content,
     * their text content can be checked by this method
     *
     * @param elementName name of element inside the accordion content
     * @param accordionName accordion name
     * @param expectedText expected accordion content text
     * @return Returns itself
     */
    public AccordionSteps checkContentElement(String elementName, String accordionName, String expectedText) {
        String actualText = getElement(accordionName).getContent().getElement(elementName).getText();
        checkText(actualText, expectedText, SearchStrategy.EQUALS);
        return this;
    }

    /**
     * For complex accordion.
     * If there are elements within the complex element of an accordion, their text content can be checked by this method
     *
     * @param accordionName accordion name
     * @param expectedTable expected accordion elements values
     * @return Returns itself
     */
    public AccordionSteps checkElements(String accordionName, DataTable expectedTable) {
        Map<String, String> expectedMap = expectedTable.asMap(String.class, String.class);
        BaseElement content = getElement(accordionName).getContent();

        expectedMap.forEach((element, elementValue) -> {
            String actualText = content.getElement(element).getText();
            checkText(actualText, elementValue, SearchStrategy.EQUALS);
        });
        return this;
    }

    private Accordion getElement(String accordionName) {
        return getFindUtils().find(accordionName, Accordion.class);
    }
}
