package ru.sbtqa.tag.pagefactory;

import ru.sbtqa.tag.pagefactory.exceptions.SwipeException;
import ru.sbtqa.tag.qautils.strategies.DirectionStrategy;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

public class StepDefs {

    /**
     * Swipe until text is visible
     *
     * @param direction direction to swipe
     * @param text text on page to swipe to
     * @throws SwipeException if the text is not found or swipe depth is reached
     */
    public void swipeToText(String direction, String text) throws SwipeException {
        MobileExtension.swipeToText(DirectionStrategy.valueOf(direction.toUpperCase()), text);
    }

    /**
     * Swipe until text is visible for Android
     *
     * @param strategy contains or exact
     * @param text text on page to swipe to
     * @throws SwipeException if the text is not found
     */
    public void swipeToTextAndroid(String text, String strategy) throws SwipeException {
        MobileExtension.swipeToText(MatchStrategy.valueOf(strategy), text);
    }
}
