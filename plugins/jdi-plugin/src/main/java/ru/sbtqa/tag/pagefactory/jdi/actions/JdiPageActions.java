package ru.sbtqa.tag.pagefactory.jdi.actions;

import com.epam.jdi.uitests.web.selenium.elements.common.Button;
import com.epam.jdi.uitests.web.selenium.elements.common.Input;
import com.epam.jdi.uitests.web.selenium.elements.complex.Dropdown;
import ru.sbtqa.tag.pagefactory.web.actions.WebPageActions;

public class JdiPageActions extends WebPageActions {

    @Override
    public void fill(Object element, String text) {
        ((Input) element).sendKeys(text);
    }

    @Override
    public void click(Object element) {
        ((Button) element).click();
    }

    @Override
    public void select(Object element, String option) {
        ((Dropdown) element).select(option);
    }
}
