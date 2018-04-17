package ru.sbtqa.tag.pagefactory.actions;

import org.openqa.selenium.WebElement;

public interface PageActions {

    void fill(WebElement webElement, String text);
    void click(WebElement webElement);
    void press(WebElement webElement, String key);
    void select(WebElement webElement, String option);
    void setCheckbox(WebElement webElement, boolean state);
}
