package ru.sbtqa.tag.pagefactory.support;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.yandex.qatools.allure.annotations.Attachment;

public class ScreenShooter {

    private static final Logger LOG = LoggerFactory.getLogger(ScreenShooter.class);

    /**
     * Takes screenshot with driver
     *
     * @return screenshot in byte array
     */
    @Attachment(type = "image/png", value = "Screenshot")
    public static byte[] takeWithDriver() {
        return ((TakesScreenshot) PageFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Takes whole screen screenshot
     *
     * @return screenshot in byte array
     */
    @Attachment(type = "image/png", value = "Full Screen Screenshot")
    public static byte[] takeRaw() {
        try {
            Rectangle screenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage picture = new Robot().createScreenCapture(screenBounds);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ImageIO.write(picture, "png", bytes);
            return bytes.toByteArray();
        } catch (AWTException | IOException ex) {
            LOG.error("Failed to get full screenshot on test failure because of IOException", ex);
            return "".getBytes();
        }
    }

}
