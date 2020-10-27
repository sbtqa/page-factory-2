package ru.sbtqa.tag.pagefactory.utils;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class Conditions {

    public static ExpectedCondition<Boolean> absenceOfElementLocated(
            final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    element.isDisplayed();
                    return false;
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    return true;
                }
            }

            @Override
            public String toString() {
                return "The element exists on page: " + element;
            }
        };
    }
}
