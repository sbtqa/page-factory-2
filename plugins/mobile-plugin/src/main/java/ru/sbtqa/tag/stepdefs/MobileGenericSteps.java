package ru.sbtqa.tag.stepdefs;

import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.exceptions.SwipeException;
import ru.sbtqa.tag.pagefactory.mobile.utils.SwipeUtils;
import ru.sbtqa.tag.qautils.strategies.DirectionStrategy;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

/**
 * Basic step definitions, that should be available on every project Notations
 * used in this class: Page - a class that extends {@link ru.sbtqa.tag.pagefactory.Page} and has
 * {@link ru.sbtqa.tag.pagefactory.annotations.ElementTitle} annotation Action -
 * a method with {@link ru.sbtqa.tag.pagefactory.annotations.ActionTitle}
 * annotation in page object List - list of {@link WebElement}'s with
 * {@link ru.sbtqa.tag.pagefactory.annotations.ElementTitle} annotation on page
 * object
 * <p>
 * To pass a Cucumber DataTable as a parameter to method,
 * supply a table in the following format after a step ini feature:
 * <p>
 * | header 1| header 2 | | value 1 | value 2 |
 * <p>
 * This table will be converted to a DataTable object.
 * First line is not enforced to be a header.
 * <p>
 * To pass a list as parameter, use flattened table as follows: | value 1 | }
 * value 2 |
 *
 * @param <T> type of steps - any successor {@code WebGenericSteps}
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
 */
public class MobileGenericSteps<T extends MobileGenericSteps<T>> extends CoreGenericSteps<T> {

    public MobileGenericSteps() {
        MobileSetupSteps.initMobile();
    }

    /**
     * Swipe until text is visible
     *
     * @param direction direction to swipe
     * @param text text on page to swipe to
     * @throws SwipeException if the text is not found or swipe depth is reached
     */
    public T swipeToTextByDirection(String direction, String text) throws SwipeException {
        SwipeUtils.swipeToText(DirectionStrategy.valueOf(direction.toUpperCase()), text);
        return (T) this;
    }

    /**
     * Swipe until text is visible for Android
     *
     * @param strategy contains or exact
     * @param text text on page to swipe to
     * @throws SwipeException if the text is not found
     */
    public T swipeToTextByMatch(String strategy, String text) throws SwipeException {
        SwipeUtils.swipeToText(MatchStrategy.valueOf(strategy), text);
        return (T) this;
    }
}
