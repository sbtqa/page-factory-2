package ru.sbtqa.tag.pagefactory.jdi.actions;

import com.epam.jdi.uitests.web.selenium.elements.complex.Dropdown;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.web.actions.WebPageActions;

public class JdiPageActions extends WebPageActions {

    @Override
    public void select(WebElement webElement, String option) {
        ((Dropdown) webElement).select(option);
    }
}
