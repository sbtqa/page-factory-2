package ru.sbtqa.tag.stepdefs;

import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflection;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

public class HtmlSetupSteps {

    private static final ThreadLocal<Boolean> isHtmlInited = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Boolean> isHtmlDisposed = ThreadLocal.withInitial(() -> false);

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    public void initHtml() {
        isAlreadyPerformed(isHtmlInited);
        if (isNewDriverNeeded()) {
            Environment.setDriverService(new WebDriverService());
        }
        Environment.setReflection(new HtmlReflection());
    }

    private boolean isNewDriverNeeded() {
        return Environment.isDriverEmpty()
                || (!Environment.isDriverEmpty() && !PROPERTIES.getShared());
    }

    public synchronized void disposeHtml() {
        isAlreadyPerformed(isHtmlDisposed);
    }

    private synchronized boolean isAlreadyPerformed(ThreadLocal<Boolean> t) {
        if (t.get()) {
            return true;
        } else {
            t.set(true);
            if (t.equals(isHtmlInited)) {
                isHtmlDisposed.remove();
            } else if (t.equals(isHtmlDisposed)) {
                isHtmlInited.remove();
            }
            return false;
        }
    }
}
