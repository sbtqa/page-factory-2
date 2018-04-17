package ru.sbtqa.tag.pagefactory.checks;

import org.openqa.selenium.WebElement;

public interface PageChecks {

    boolean checkEquality(WebElement webElement, String text);
    boolean checkEmptiness(WebElement webElement);
}
