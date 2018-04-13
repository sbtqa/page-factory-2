package ru.sbtqa.tag.pagefactory;

import ru.sbtqa.tag.pagefactory.drivers.DriverService;

// Статичное хранилище для инфраструктуры
public class Environment {

    private static DriverService driverService;
//    private static Configuration properties;

    public static void setDriverService(DriverService driverService) {
        Environment.driverService = driverService;
    }

    public static DriverService getDriverService() {
        return driverService;
    }

//    public static Configuration getProperties() {
//        return properties;
//    }
//
//    public static void setProperties(Configuration properties) {
//        Environment.properties = properties;
//    }
}
