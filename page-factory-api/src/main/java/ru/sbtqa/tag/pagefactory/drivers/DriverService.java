package ru.sbtqa.tag.pagefactory.drivers;

import org.openqa.selenium.WebDriver;

public interface DriverService {

    void mountDriver();
    void demountDriver();
    <T extends WebDriver> T getDriver();
    boolean isDriverEmpty();
}
