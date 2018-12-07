package ru.sbtqa.tag.pagefactory.environment;

import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.checks.PageChecks;
import ru.sbtqa.tag.pagefactory.drivers.DriverService;
import ru.sbtqa.tag.pagefactory.reflection.Reflection;

/**
 * Static storage for infrastructure
 */
public class Environment {

    private static DriverService driverService;
    private static PageActions pageActions;
    private static PageChecks pageChecks;
    private static Reflection reflection;

    public static void setDriverService(DriverService driverService) {
        Environment.driverService = driverService;
    }

    public static DriverService getDriverService() {
        return driverService;
    }

    public static boolean isDriverEmpty() {
        return driverService == null || driverService.isDriverEmpty();
    }

    public static PageActions getPageActions() {
        return pageActions;
    }

    public static void setPageActions(PageActions pageActions) {
        Environment.pageActions = pageActions;
    }

    public static PageChecks getPageChecks() {
        return pageChecks;
    }

    public static void setPageChecks(PageChecks pageChecks) {
        Environment.pageChecks = pageChecks;
    }

    public static Reflection getReflection() {
        return reflection;
    }

    public static void setReflection(Reflection reflection) {
        if (Environment.reflection == null || !reflection.getClass().isAssignableFrom(Environment.reflection.getClass())) {
            Environment.reflection = reflection;
        }
    }
}
