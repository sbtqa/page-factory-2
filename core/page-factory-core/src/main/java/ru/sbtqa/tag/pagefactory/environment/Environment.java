package ru.sbtqa.tag.pagefactory.environment;

import ru.sbtqa.tag.pagefactory.drivers.DriverService;

// Статичное хранилище для инфраструктуры
public class Environment {

    private static DriverService driverService;

    public static void setDriverService(DriverService driverService) {
        Environment.driverService = driverService;
    }

    public static DriverService getDriverService() {
        return driverService;
    }
}
