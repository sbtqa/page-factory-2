package ru.sbtqa.tag.pagefactory.drivers;

import org.openqa.selenium.WebDriver;

public interface DriverService {

    WebDriver mountDriver();
    void demountDriver();
}
