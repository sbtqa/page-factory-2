package ru.sbtqa.tag.pagefactory.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.environment.Environment;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public enum ScreenshotUtils {
    DRIVER {
        public byte[] take() {
            WebDriver webDriver = Environment.getDriverService().getDriver();
            return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
        }
    },
    RAW {
        public byte[] take() {
            try {
                Rectangle screenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage picture = new Robot().createScreenCapture(screenBounds);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                ImageIO.write(picture, "png", bytes);
                return bytes.toByteArray();
            } catch (AWTException | IOException ex) {
                Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
                log.error("Failed to get full screenshot on test failure because of IOException", ex);
                return "".getBytes();
            }
        }
    };

    public abstract byte[] take();
}
