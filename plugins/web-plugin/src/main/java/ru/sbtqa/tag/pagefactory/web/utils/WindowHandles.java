package ru.sbtqa.tag.pagefactory.web.utils;

import java.util.Set;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.web.properties.WebConfiguration;

public class WindowHandles {

    private static final WebConfiguration PROPERTIES = WebConfiguration.create();

    private WindowHandles() {}

    /**
     * Find new window handle
     *
     * @param existingHandles an existing handles
     * @return the new window handle
     * @throws WaitException in case if new window handle didn't find
     */
    public static String findNewWindowHandle(Set<String> existingHandles) throws WaitException, InterruptedException {
        return findNewWindowHandle(existingHandles, PROPERTIES.getTimeout());
    }

    /**
     * Find new window handle
     *
     * @param existingHandles an existing handles
     * @param timeout timeout
     * @return the new window handle
     * @throws WaitException in case if new window handle didn't find
     */
    public static String findNewWindowHandle(Set<String> existingHandles, int timeout) throws WaitException, InterruptedException {
        long timeoutTime = System.currentTimeMillis() + timeout * 1000L;

        while (timeoutTime > System.currentTimeMillis()) {
            Set<String> currentHandles = Environment.getDriverService().getDriver().getWindowHandles();

            if (currentHandles.size() != existingHandles.size()|| !currentHandles.equals(existingHandles)) {
                for (String currentHandle : currentHandles) {
                    if (!existingHandles.contains(currentHandle)) {
                        return currentHandle;
                    }
                }
            }

            Thread.sleep(1000);
        }

        throw new WaitException("Timed out after '" + timeout + "' seconds waiting for new modal window");
    }
}
