package ru.sbtqa.tag.pagefactory;

import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.jdi.actions.JdiPageActions;
import ru.sbtqa.tag.pagefactory.jdi.utils.JDIUtils;

/**
 * Inherit your jdi page objects from this class
 */
public abstract class JDIPage implements Page {

    private static PageActions pageActions = new JdiPageActions();

    public JDIPage() {
        Environment.setPageActions(pageActions);

        JDIUtils.initElementsOnPage(this);
    }
}
