package ru.sbtqa.tag.pagefactory.web.checks;

import static ru.sbtqa.tag.pagefactory.web.utils.ElementUtils.getWebElementValue;

import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.checks.PageChecks;
import ru.sbtqa.tag.pagefactory.web.utils.WebWait;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

public class WebPageChecks implements PageChecks {

    private static final String WHITESPACES = "\\s+";
    private static final String EMPTY_STRING = "";

    @Override
    public boolean checkEquality(Object element, String text) {
        WebElement webElement = (WebElement) element;
        WebWait.visibility(webElement);
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

    @Override
    public boolean checkEmptiness(Object element) {
        WebElement webElement = (WebElement) element;
        WebWait.visibility(webElement);
        String value = getWebElementValue(webElement);
        return "".equals(value) || value.isEmpty();
    }
}
