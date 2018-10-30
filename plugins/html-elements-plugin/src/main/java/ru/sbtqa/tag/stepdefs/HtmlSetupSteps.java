package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflectionImpl;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

public class HtmlSetupSteps {

    private static final ThreadLocal<Boolean> isHtmlInited = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Boolean> isHtmlDisposed = ThreadLocal.withInitial(() -> false);

    public void initHtml() {
        isAlreadyPerformed(isHtmlInited);

        Environment.setDriverService(new WebDriverService());
        Environment.setReflection(new HtmlReflectionImpl());
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
