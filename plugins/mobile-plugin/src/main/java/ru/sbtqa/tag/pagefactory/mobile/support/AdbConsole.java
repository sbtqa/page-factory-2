package ru.sbtqa.tag.pagefactory.mobile.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.drivers.MobileDriverService;

public class AdbConsole {

    private static final Logger LOG = LoggerFactory.getLogger(AdbConsole.class);

    private AdbConsole() {
        throw new IllegalAccessError("Utility class");
    }

    public static boolean execute(String command) {
        return execute(((MobileDriverService) Environment.getDriverService()).getDeviceUDID(), command);
    }

    public static boolean execute(String deviceUDID, String command) {
        ProcessBuilder processBuilder = new ProcessBuilder("adb", "-s", deviceUDID, "shell", command);
        LOG.info("Command '{}' is processing...", command);
        try {
            Process process = processBuilder.start();

            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            LOG.debug(builder.toString());

            return process.waitFor() == 0;
        } catch (IOException | InterruptedException ex) {
            LOG.error("Failed to process command '{}'", command, ex);
            Thread.currentThread().interrupt();
        }

        return false;
    }
}
