package ru.sbtqa.tag.pagefactory.mobile.junit;

import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSTouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.SwipeException;
import ru.sbtqa.tag.pagefactory.junit.CoreStepsImpl;
import ru.sbtqa.tag.pagefactory.mobile.utils.SwipeUtils;
import ru.sbtqa.tag.qautils.strategies.DirectionStrategy;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

import static io.appium.java_client.ios.touch.IOSPressOptions.iosPressOptions;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static java.time.Duration.ofMillis;

public class MobileStepsImpl<T extends MobileStepsImpl<T>> extends CoreStepsImpl<T> {

    public MobileStepsImpl() {
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

    /**
     * User make touch action tap on the element's center
     *
     * @param elementTitle title of the element
     */
    public T press(String elementTitle) throws PageException {
        WebElement element = Environment.getFindUtils().getElementByTitle(PageContext.getCurrentPage(), elementTitle);

        Rectangle rect = element.getRect();
        int centerX = rect.getX() + rect.getWidth() / 2;
        int centerY = rect.getY() + rect.getHeight() / 2;

        TouchAction touchAction = new TouchAction(Environment.getDriverService().getDriver());
        touchAction.tap(PointOption.point(centerX, centerY)).perform();
        return (T) this;
    }

    /**
     * User press on the element
     *
     * @param elementTitle title of the element
     */
    public T tap(String elementTitle) throws PageException {
        WebElement element = Environment.getFindUtils().getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        tap(element);
        return (T) this;
    }

    /**
     * User press on the element
     *
     * @param element element
     */
    public T tap(WebElement element) {
        new IOSTouchAction(Environment.getDriverService().getDriver())
                .press(iosPressOptions()
                        .withElement(element(element))
                        .withPressure(1))
                .waitAction(waitOptions(ofMillis(100)))
                .release()
                .perform();
        return (T) this;
    }
}
