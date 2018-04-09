package ru.sbtqa.tag.pagefactory;

import ru.sbtqa.tag.pagefactory.drivers.DriverService;

// Статичное хранилище для инфраструктуры
public class TestEnvironment {

    private static DriverService driverService;

    public static void setDriverService(DriverService driverService) {
        TestEnvironment.driverService = driverService;
    }

    public DriverService getDriverService() {
        return driverService;
    }
}
