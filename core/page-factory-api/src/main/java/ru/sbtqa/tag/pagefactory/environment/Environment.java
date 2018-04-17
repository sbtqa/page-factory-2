package ru.sbtqa.tag.pagefactory.environment;

import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.drivers.DriverService;

// Статичное хранилище для инфраструктуры
public class Environment {

    private static DriverService driverService;
    private static PageActions pageActions;

    public static void setDriverService(DriverService driverService) {
        Environment.driverService = driverService;
    }

    public static DriverService getDriverService() {
        return driverService;
    }

    public static PageActions getPageActions() {
        return pageActions;
    }

    public static void setPageActions(PageActions pageActions) {
        Environment.pageActions = pageActions;
    }
}
