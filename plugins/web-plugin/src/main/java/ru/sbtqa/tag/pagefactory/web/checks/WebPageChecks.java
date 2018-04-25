package ru.sbtqa.tag.pagefactory.web.checks;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import ru.sbtqa.tag.pagefactory.checks.PageChecks;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

public class WebPageChecks implements PageChecks {

    private static final String WHITESPACES = "\\s+";
    private static final String EMPTY_STRING = "";
    private static final String INPUT = "input";
    private static final String VALUE = "value";
    private static final String SELECT = "select";
    private static final String TITLE = "title";

    @Override
    public boolean checkEquality(Object element, String text) {
        WebElement webElement = (WebElement) element;
        return checkEquality(webElement, text, MatchStrategy.EXACT);
    }

    public boolean checkEquality(Object element, String text, MatchStrategy matchStrategy) {
        WebElement webElement = (WebElement) element;
        String value = getWebElementValue(webElement);

        if (matchStrategy == MatchStrategy.EXACT) {
            return text.replaceAll(WHITESPACES, EMPTY_STRING).equals(value.replaceAll(WHITESPACES, EMPTY_STRING));
        } else {
            return value.replaceAll(WHITESPACES, EMPTY_STRING).contains(text.replaceAll(WHITESPACES, EMPTY_STRING));
        }
    }

    private String getWebElementValue(WebElement webElement) {
        switch (webElement.getTagName()) {
            case INPUT:
                return webElement.getAttribute(VALUE);
            case SELECT:
                Select select = new Select(webElement);
                return select.getFirstSelectedOption().getText();
            default:
                return webElement.getText();
        }
    }

    @Override
    public boolean checkEmptiness(Object element) {
        WebElement webElement = (WebElement) element;
        String value = getWebElementValue(webElement);
        return "".equals(value) || value.isEmpty();
    }
}
