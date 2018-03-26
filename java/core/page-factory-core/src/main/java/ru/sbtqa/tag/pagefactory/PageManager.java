package ru.sbtqa.tag.pagefactory;

import com.google.common.reflect.ClassPath;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.drivers.TagMobileDriver;
import ru.sbtqa.tag.pagefactory.drivers.TagWebDriver;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.support.Environment;
import ru.sbtqa.tag.qautils.errors.AutotestError;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class PageManager {

    private static final Logger LOG = LoggerFactory.getLogger(PageManager.class);

    private static String pagesPackage;

    public PageManager(String pagesPackage) {
        this.pagesPackage = pagesPackage;
    }

    /**
     * Initialize page by class
     *
     * @param page TODO
     * @return TODO
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException
     * TODO
     */
    public Page getPage(Class<? extends Page> page, WebDriver driver) throws PageInitializationException {
        return bootstrapPage(page, driver);
    }

    /**
     * Initialize page with specified title and save its instance to
     * {@link PageContext#currentPage} for further use
     *
     * @param title page title
     * @return page instance
     * @throws PageInitializationException if failed to execute corresponding
     * page constructor
     */
    public Page getPage(String title) throws PageInitializationException {

        if (null == PageContext.getCurrentPage() || !PageContext.getCurrentPageTitle().equals(title)) {
            if (null != PageContext.getCurrentPage()) {
                getPage(pagesPackage, title, PageContext.getCurrentPage().getDriver());
            }
            if (null == PageContext.getCurrentPage()) {
                Environment environment = PageFactory.getEnvironment();
                switch (environment) {
                    case WEB:
                        getPage(pagesPackage, title, TagWebDriver.getDriver());
                        break;
                    case MOBILE:
                        getPage(pagesPackage, title, TagMobileDriver.getDriver());
                        break;
                    default:
                        throw new AutotestError("Unknown environment" + environment);
                }
            }
            if (null == PageContext.getCurrentPage()) {
                throw new AutotestError("WebElementsPage object with title '" + title + "' is not registered");
            }
        }
        return PageContext.getCurrentPage();
    }

    /**
     * Redirect to WebElementsPage by WebElementsPage Entry url value
     *
     * @param packageName a {@link java.lang.String} object.
     * @param title a {@link java.lang.String} object.
     * @return a WebElementsPage object.
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException
     * TODO
     */
    public static Page changeUrlByTitle(String packageName, String title) throws PageInitializationException {

        Class<?> pageClass = getPageClass(packageName, title);
        if (pageClass == null) {
            return null;
        }

        Annotation annotation = pageClass.getAnnotation(PageEntry.class);
        if (annotation != null && !((PageEntry) annotation).url().isEmpty()) {
            if (PageContext.getCurrentPage().getDriver().getCurrentUrl() == null) {
                throw new AutotestError("Current URL is null");
            } else {
                try {
                    URL currentUrl = new URL(PageContext.getCurrentPage().getDriver().getCurrentUrl());
                    String finalUrl = new URL(currentUrl.getProtocol(), currentUrl.getHost(), currentUrl.getPort(),
                            ((PageEntry) annotation).url()).toString();
                    PageContext.getCurrentPage().getDriver().navigate().to(finalUrl);
                } catch (MalformedURLException ex) {
                    LOG.error("Failed to get current url", ex);
                }
            }

            return bootstrapPage(pageClass, PageContext.getCurrentPage().getDriver());
        }

        throw new AutotestError("WebElementsPage " + title + " doesn't have fast URL in PageEntry");
    }

    /**
     * Redirect to WebElementsPage by WebElementsPage Entry url value
     *
     * @param title a {@link java.lang.String} object.
     * @return a WebElementsPage object.
     * @throws PageInitializationException TODO
     */
    public static Page changeUrlByTitle(String title) throws PageInitializationException {
        if (null != PageContext.getCurrentPage()) {
            PageContext.setCurrentPage(changeUrlByTitle(pagesPackage, title));
        }
        if (null == PageContext.getCurrentPage()) {
            PageContext.setCurrentPage(changeUrlByTitle(pagesPackage, title));
        }
        if (null == PageContext.getCurrentPage()) {
            throw new AutotestError("WebElementsPage Object with title " + title + " is not registered");
        }
        return PageContext.getCurrentPage();
    }

    /**
     * <p>
     * Get WebElementsPage by PageEntry title </p>
     *
     * @param packageName a {@link java.lang.String} object.
     * @param title a {@link java.lang.String} object.
     * @return a WebElementsPage object.
     * @throws PageInitializationException {@inheritDoc}
     */
    public Page getPage(String packageName, String title, WebDriver driver) throws PageInitializationException {
        return bootstrapPage(getPageClass(packageName, title), driver);
    }

    /**
     *
     * @param packageName TODO
     * @param title TODO
     * @return
     */
    private static Class<?> getPageClass(final String packageName, String title) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final Set<Class<?>> allClasses = new HashSet<>();
        // TODO: var add cash 5/29/17
        try {
            for (ClassPath.ClassInfo info : ClassPath.from(loader).getAllClasses()) {
                if (info.getName().startsWith(packageName + ".")) {
                    allClasses.add(info.load());
                }
            }
        } catch (IOException ex) {
            LOG.warn("Failed to shape class info set", ex);
        }

        for (Class<?> page : allClasses) {
            String pageTitle = null;
            if (null != page.getAnnotation(PageEntry.class)) {
                pageTitle = page.getAnnotation(PageEntry.class).title();
            } else {
                try {
                    pageTitle = (String) FieldUtils.readStaticField(page, "title", true);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    LOG.debug("Failed to read {} becase it is not page object", pageTitle, ex);
                }
            }
            if (pageTitle != null && pageTitle.equals(title)) {
                return page;
            }
        }

        return null;
    }

    /**
     * Run constructor of specified page class and put its instance into static
     * {@link PageContext#currentPage} variable
     *
     * @param page page class
     * @return initialized page
     * @throws PageInitializationException if failed to execute corresponding
     * page constructor
     */
    private static Page bootstrapPage(Class<?> page, WebDriver driver) throws PageInitializationException {
        if (page != null) {
            try {
                @SuppressWarnings("unchecked")
                Constructor<Page> constructor = ((Constructor<Page>) page.getConstructor(WebDriver.class));
                constructor.setAccessible(true);
                Page currentPage = constructor.newInstance(driver);
                PageContext.setCurrentPage(currentPage);
                return currentPage;
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new PageInitializationException("Failed to initialize page '" + page + "'", e);
            }
        }
        return null;
    }
}
