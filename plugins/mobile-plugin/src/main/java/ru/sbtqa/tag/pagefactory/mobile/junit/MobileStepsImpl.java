package ru.sbtqa.tag.pagefactory.mobile.junit;

import ru.sbtqa.tag.pagefactory.exceptions.SwipeException;
import ru.sbtqa.tag.pagefactory.junit.CoreStepsImpl;
import ru.sbtqa.tag.pagefactory.mobile.utils.SwipeUtils;
import ru.sbtqa.tag.qautils.strategies.DirectionStrategy;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

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
}
