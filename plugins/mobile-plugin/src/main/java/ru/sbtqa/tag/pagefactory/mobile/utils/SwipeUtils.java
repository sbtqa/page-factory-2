package ru.sbtqa.tag.pagefactory.mobile.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import java.util.List;

import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.SwipeException;
import ru.sbtqa.tag.qautils.strategies.DirectionStrategy;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

public class SwipeUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SwipeUtils.class);

    private static final int DEFAULT_SWIPE_TIME = 3000;
    private static final int DEFAULT_SWIPE_DEPTH = 256;
    private static final double INDENT_BOTTOM = 0.80;
    private static final double INDENT_TOP = 0.20;
    private static final double INDENT_LEFT = 0.30;
    private static final double INDENT_RIGHT = 0.70;

    private static final AppiumDriver appiumDriver = Environment.getDriverService().getDriver();

    private SwipeUtils() {
    }

    /**
     * Swipe element to direction
     *
     * @param element element to swipe
     * @param direction swipe direction
     * @throws SwipeException if there is an error while swiping
     */
    public static void swipe(WebElement element, DirectionStrategy direction) throws SwipeException {
        swipe(element, direction, DEFAULT_SWIPE_TIME);
    }

    /**
     * Swipe element to direction
     *
     * @param element target element to swipe
     * @param direction swipe direction
     * @param time how fast element should be swiped
     * @throws SwipeException if there is an error while swiping
     */
    public static void swipe(WebElement element, DirectionStrategy direction, int time) throws SwipeException {
        Dimension size = element.getSize();
        Point location = element.getLocation();
        swipe(location, size, direction, time);
    }

    /**
     * Swipe screen to direction
     *
     * @param direction swipe direction
     * @throws SwipeException if there is an error while swiping
     */
    public static void swipe(DirectionStrategy direction) throws SwipeException {
        swipe(direction, DEFAULT_SWIPE_TIME);
    }

    /**
     * Swipe screen to direction
     *
     * @param direction swipe direction
     * @param time how fast screen should be swiped
     * @throws SwipeException if there is an error while swiping
     */
    public static void swipe(DirectionStrategy direction, int time) throws SwipeException {
        Dimension size = appiumDriver.manage().window().getSize();
        swipe(new Point(0, 0), size, direction, time);
    }

    /**
     * Swipe to direction
     *
     * @param location top left-hand corner of the element
     * @param size width and height of the element
     * @param direction swipe direction
     * @param time how fast screen should be swiped
     * @throws SwipeException if there is an error while swiping
     */
    private static void swipe(Point location, Dimension size, DirectionStrategy direction, int time) throws SwipeException {
        int startx;
        int endx;
        int starty;
        int endy;
        switch (direction) {
            case DOWN:
                startx = endx = size.width / 2;
                starty = (int) (size.height * INDENT_BOTTOM);
                endy = (int) (size.height * INDENT_TOP);
                break;
            case UP:
                startx = endx = size.width / 2;
                starty = (int) (size.height * INDENT_TOP);
                endy = (int) (size.height * INDENT_BOTTOM);
                break;
            case RIGHT:
                startx = (int) (size.width * INDENT_RIGHT);
                endx = (int) (size.width * INDENT_LEFT);
                starty = endy = size.height / 2;
                break;
            case LEFT:
                startx = (int) (size.width * INDENT_LEFT);
                endx = (int) (size.width * INDENT_RIGHT);
                starty = endy = size.height / 2;
                break;
            default:
                throw new SwipeException("Failed to swipe to direction " + direction);
        }

        int x = location.getX();
        int y = location.getY();
        LOG.debug("Swipe parameters: location {}, dimension {}, direction {}, time {}", location, size, direction, time);
        new TouchAction(Environment.getDriverService().getDriver())
                .press(PointOption.point(x + startx, y + starty))
                .waitAction()
                .moveTo(PointOption.point(x + endx, y + endy))
                .release()
                .perform();
    }

    /**
     * Swipe until the text becomes visible
     *
     * @param direction swipe direction
     * @param text text on page to swipe to
     * @throws SwipeException if there is an error while swiping
     */
    public static void swipeToText(DirectionStrategy direction, String text) throws SwipeException {
        swipeToText(direction, text, MatchStrategy.EXACT);
    }

    /**
     * Swipe until the text becomes visible
     *
     * @param direction swipe direction
     * @param text text on page to swipe to
     * @param strategy contains or exact
     * @throws SwipeException if there is an error while swiping
     */
    public static void swipeToText(DirectionStrategy direction, String text, MatchStrategy strategy) throws SwipeException {
        swipeToText(direction, text, strategy, DEFAULT_SWIPE_DEPTH);
    }

    /**
     * Swipe until the text becomes visible
     *
     * @param direction swipe direction
     * @param text text on page to swipe to
     * @param strategy contains or exact
     * @param depth the amount of swipe action
     * @throws SwipeException if there is an error while swiping
     */
    public static void swipeToText(DirectionStrategy direction, String text, MatchStrategy strategy, int depth) throws SwipeException {
        for (int depthCounter = 0; depthCounter < depth; depthCounter++) {
            String oldPageSource = appiumDriver.getPageSource();
            if (strategy == MatchStrategy.EXACT) {
                if (!appiumDriver.findElements(By.xpath("//*[@text='" + text + "']")).isEmpty()) {
                    return;
                }
            } else {
                List<WebElement> textViews = appiumDriver.findElements(By.className("android.widget.TextView"));
                if (!textViews.isEmpty()) {
                    for (WebElement textView : textViews) {
                        if (textView.getText().contains(text)) {
                            return;
                        }
                    }
                }
            }
            swipe(direction);

            if (appiumDriver.getPageSource().equals(oldPageSource)) {
                throw new SwipeException("Swiping limit is reached. Text not found");
            }
        }

        throw new SwipeException("Swiping depth is reached. Text not found");
    }

    /**
     * Swipe until the text becomes visible
     *
     * UIAutomator for Android. Much faster and stable
     *
     * @param text text on page to swipe to
     * @param strategy contains or exact
     * @throws SwipeException if there is an error while swiping
     */
    public static void swipeToText(MatchStrategy strategy, String text) throws SwipeException {
        switch (strategy) {
            case EXACT:
                try {
                    appiumDriver.findElement(MobileBy
                            .AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0))"
                                    + ".scrollIntoView(new UiSelector().text(\"" + text + "\").instance(0))"));
                } catch (NoSuchElementException e) {
                    throw new SwipeException("Swiping limit is reached. Text: " + text + " not found");
                }
                break;
            case CONTAINS:
                try {
                    appiumDriver.findElement(MobileBy
                            .AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0))"
                                    + ".scrollIntoView(new UiSelector().textContains(\"" + text + "\").instance(0))"));
                } catch (NoSuchElementException e) {
                    throw new SwipeException("Swiping limit is reached. Part text: " + text + " not found");
                }
                break;
            default:
                throw new SwipeException("Please use correct matching strategy. Available options: 'EXACT' or 'CONTAINS'.");
        }
    }
}
