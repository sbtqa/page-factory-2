package ru.sbtqa.tag.pagefactory;

import ru.sbtqa.tag.pagefactory.drivers.DriverService;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

// Статичное хранилище для инфраструктуры
public class TestEnvironment {

    private static DriverService driverService;
    private static Configuration properties;

    public static void setDriverService(DriverService driverService) {
        TestEnvironment.driverService = driverService;
    }

    public static DriverService getDriverService() {
        return driverService;
    }

    public static Configuration getProperties() {
        return properties;
    }

    public static void setProperties(Configuration properties) {
        TestEnvironment.properties = properties;
    }
}
