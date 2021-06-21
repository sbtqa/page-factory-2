package ru.sbtqa.tag.pagefactory.environment;

import cucumber.api.Scenario;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.checks.PageChecks;
import ru.sbtqa.tag.pagefactory.drivers.DriverService;
import ru.sbtqa.tag.pagefactory.find.Find;
import ru.sbtqa.tag.pagefactory.reflection.Reflection;

/**
 * Static storage for infrastructure
 */
public class Environment {

    private static final ThreadLocal<DriverService> driverService = new ThreadLocal<>();
    private static final ThreadLocal<PageActions> pageActions = new ThreadLocal<>();
    private static final ThreadLocal<PageChecks> pageChecks = new ThreadLocal<>();
    private static final ThreadLocal<Reflection> reflection = new ThreadLocal<>();
    private static final ThreadLocal<Find> findUtils = new ThreadLocal<>();
    private static final ThreadLocal<Scenario> scenario = new ThreadLocal<>();

    public static void setDriverService(DriverService driverService) {
        Environment.driverService.set(driverService);
    }

    @SuppressWarnings("unchecked")
    public static <T extends DriverService> T getDriverService() {
        return (T) driverService.get();
    }

    public static boolean isDriverEmpty() {
        return driverService.get() == null || driverService.get().isDriverEmpty();
    }

    @SuppressWarnings("unchecked")
    public static <T extends PageActions> T getPageActions() {
        return (T) pageActions.get();
    }

    public static void setPageActions(PageActions pageActions) {
        Environment.pageActions.set(pageActions);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PageChecks> T getPageChecks() {
        return (T) pageChecks.get();
    }

    public static void setPageChecks(PageChecks pageChecks) {
        Environment.pageChecks.set(pageChecks);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Reflection> T getReflection() {
        return (T) reflection.get();
    }

    public static void setReflection(Reflection reflection) {
        Environment.reflection.set(reflection);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Find> T getFindUtils() {
        return (T) findUtils.get();
    }

    public static void setFindUtils(Find findUtils) {
        Environment.findUtils.set(findUtils);
    }

    public static Scenario getScenario() {
        return scenario.get();
    }

    public static void setScenario(Scenario scenario) {
        Environment.scenario.set(scenario);
    }

    public static void clearEnvironment() {
        Environment.pageActions.remove();
        Environment.pageChecks.remove();
        Environment.reflection.remove();
        Environment.findUtils.remove();
        Environment.scenario.remove();
    }

    public static void clearDriverService() {
        Environment.driverService.remove();
    }
}
