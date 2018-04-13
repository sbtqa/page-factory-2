package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

public class PageFactory {

    public static void initElements(WebDriver driver, Object page) {
        org.openqa.selenium.support.PageFactory.initElements(driver, page);
    }

    public static void initElements(FieldDecorator decorator, Object page) {
        org.openqa.selenium.support.PageFactory.initElements(decorator, page);
    }
}
