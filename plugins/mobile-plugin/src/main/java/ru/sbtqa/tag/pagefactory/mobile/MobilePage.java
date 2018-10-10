package ru.sbtqa.tag.pagefactory.mobile;

import ru.sbtqa.tag.pagefactory.DefaultPage;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.mobile.utils.SwipeUtils;
import ru.sbtqa.tag.pagefactory.exceptions.SwipeException;
import ru.sbtqa.tag.qautils.strategies.DirectionStrategy;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

/**
 * Contains basic ru.sbtqa.tag.pagefactory.mobile.actions in particular with web elements
 * If we want to extend this functional - inherit from this class
 */
public abstract class MobilePage extends DefaultPage {

    /**
     * Swipe until text is visible
     *
     * @param direction direction to swipe
     * @param text text on page to swipe to
     * @throws SwipeException if the text is not found or swipe depth is reached
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.swipe.direction")
    public void swipeToTextByDirection(String direction, String text) throws SwipeException {
        SwipeUtils.swipeToText(DirectionStrategy.valueOf(direction.toUpperCase()), text);
    }

    /**
     * Swipe until text is visible for Android
     *
     * @param strategy contains or exact
     * @param text text on page to swipe to
     * @throws SwipeException if the text is not found
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.swipe.match")
    public void swipeToTextByMatch(String strategy, String text) throws SwipeException {
        SwipeUtils.swipeToText(MatchStrategy.valueOf(strategy), text);
    }
}