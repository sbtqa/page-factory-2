package ru.sbtqa.tag.api;

import org.openqa.selenium.WebDriver;
import ru.sbtqa.tag.pagefactory.drivers.DriverService;

public class ApiDriverService implements DriverService {

    @Override
    public WebDriver getDriver() {
        System.out.println("api getDriver");
        return null;
    }

    @Override
    public void mountDriver() {
        System.out.println("api mountDriver");
    }


    @Override
    public void demountDriver() {
        System.out.println("api demountDriver");
    }
}
