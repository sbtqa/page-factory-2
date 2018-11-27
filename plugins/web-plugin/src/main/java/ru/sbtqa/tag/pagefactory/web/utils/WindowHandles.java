package ru.sbtqa.tag.pagefactory.web.utils;

import java.util.Set;
import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

public class WindowHandles {

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    /**
     * @param existingHandles an existing handles
     * @return the new window handle
     * @throws WaitException in case if new window handle didn't find
     */
    public static String findNewWindowHandle(Set<String> existingHandles) throws WaitException, InterruptedException {
        return findNewWindowHandle(existingHandles, PROPERTIES.getTimeout());
    }

    /**
     * @param existingHandles an existing handles
     * @param timeout timeout
     * @return the new window handle
     * @throws WaitException in case if new window handle didn't find
     */
    public static String findNewWindowHandle(Set<String> existingHandles, int timeout) throws WaitException, InterruptedException {
        long timeoutTime = System.currentTimeMillis() + timeout;

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

        throw new WaitException("Timed out after '" + timeout + "' milliseconds waiting for new modal window");
    }
}
