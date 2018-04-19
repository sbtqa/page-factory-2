package ru.sbtqa.tag.pagefactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.environment.Environment;

public class AdbConsole {

    private static final Logger LOG = LoggerFactory.getLogger(AdbConsole.class);

    private AdbConsole() {
        throw new IllegalAccessError("Utility class");
    }

    public static boolean execute(String command) {
        return execute(((MobileDriverService) Environment.getDriverService()).getDeviceUDID(), command);
    }

    public static boolean execute(String deviceUDID, String command) {
        ProcessBuilder processBuilder = new ProcessBuilder(new String[]{"adb", "-s", deviceUDID, "shell", command});
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
        }

        return false;
    }
}
