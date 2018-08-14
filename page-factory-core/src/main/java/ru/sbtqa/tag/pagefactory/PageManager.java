package ru.sbtqa.tag.pagefactory;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;

public class PageManager {

    private static final Logger LOG = LoggerFactory.getLogger(PageManager.class);
    private static final Map<Class<? extends Page>, Map<Field, String>> PAGES_REPOSITORY = new HashMap<>();
    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    private PageManager() {}

    public static Map<Class<? extends Page>, Map<Field, String>> getPageRepository() {
        return PAGES_REPOSITORY;
    }

    /**
     * Initialize page by class
     *
     * @param page a page class
     * @param driver a web driver
     * @return the page object
     * @throws PageInitializationException if failed to execute corresponding page constructor
     */
    public static Page getPage(Class<? extends Page> page, WebDriver driver) throws PageInitializationException {
        return bootstrapPage(page, driver);
    }

    /**
     * Get Page by PageEntry title
     *
     * @param packageName a path to page objects
     * @param title a page title
     * @param driver a web driver
     * @return the page object
     * @throws PageInitializationException if failed to execute corresponding page constructor
     */
    public static Page getPage(String packageName, String title, WebDriver driver) throws PageInitializationException {
        return bootstrapPage(getPageClass(packageName, title), driver);
    }

    /**
     * Initialize page with specified title and save its instance to
     * {@link PageContext#currentPage} for further use
     *
     * @param title a page title
     * @return the page instance
     * @throws PageInitializationException if failed to execute corresponding page constructor
     */
    public static Page getPage(String title) throws PageInitializationException {
        if (null == PageContext.getCurrentPage() || !PageContext.getCurrentPageTitle().equals(title)) {
            if (null != PageContext.getCurrentPage()) {
                getPage(PROPERTIES.getPagesPackage(), title, PageContext.getCurrentPage().getDriver());
            }
            if (null == PageContext.getCurrentPage()) {
                getPage(PROPERTIES.getPagesPackage(), title, Environment.getDriverService().getDriver());
            }
            if (null == PageContext.getCurrentPage()) {
                throw new AutotestError("Page object with title '" + title + "' is not registered");
            }
        }
        return PageContext.getCurrentPage();
    }

    /**
     * Run constructor of specified page class and put its instance into static
     * {@link PageContext#currentPage} variable
     *
     * @param page a page class
     * @return the initialized page object
     * @throws PageInitializationException if failed to execute corresponding page constructor
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

    /**
     * @param packageName a path to page objects
     * @param title a page title
     * @return the page class
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
     * Redirect to Page by Page Entry url value
     *
     * @param title  a page title
     * @return the page object
     * @throws PageInitializationException if failed to execute corresponding page constructor
     */
    public static Page changeUrlByTitle(String title) throws PageInitializationException {
        if (null != PageContext.getCurrentPage()) {
            PageContext.setCurrentPage(changeUrlByTitle(PROPERTIES.getPagesPackage(), title));
        }
        if (null == PageContext.getCurrentPage()) {
            PageContext.setCurrentPage(changeUrlByTitle(PROPERTIES.getPagesPackage(), title));
        }
        if (null == PageContext.getCurrentPage()) {
            throw new AutotestError("Page Object with title " + title + " is not registered");
        }
        return PageContext.getCurrentPage();
    }

    /**
     * Redirect to Page by Page Entry url value
     *
     * @param packageName a path to page objects
     * @param title  a page title
     * @return the page object
     * @throws PageInitializationException if failed to execute corresponding page constructor
     */
    public static Page changeUrlByTitle(String packageName, String title) throws PageInitializationException {

        Class<?> pageClass = getPageClass(packageName, title);
        if (pageClass == null) {
            return null;
        }

        Annotation annotation = pageClass.getAnnotation(PageEntry.class);
        String currentUrl = PageContext.getCurrentPage().getDriver().getCurrentUrl();
        if (annotation != null && !((PageEntry) annotation).url().isEmpty()) {
            if (currentUrl == null) {
                throw new AutotestError("Current URL is null");
            } else {
                try {
                    URL newUrl = new URL(currentUrl);
                    String finalUrl = new URL(newUrl.getProtocol(), newUrl.getHost(), newUrl.getPort(),
                            ((PageEntry) annotation).url()).toString();
                    PageContext.getCurrentPage().getDriver().navigate().to(finalUrl);
                } catch (MalformedURLException ex) {
                    LOG.error("Failed to get current url", ex);
                }
            }

            return bootstrapPage(pageClass, PageContext.getCurrentPage().getDriver());
        }

        throw new AutotestError("Page " + title + " doesn't have fast URL in PageEntry");
    }

    public static void cachePages() {
        Set<Class<?>> allClasses = new HashSet();
        allClasses.addAll(getAllClasses());

        for (Class<?> page : allClasses) {
            List<Field> fields = FieldUtilsExt.getDeclaredFieldsWithInheritance(page);
            Map<Field, String> fieldsMap = new HashMap<>();
            for (Field field : fields) {
                ElementTitle titleAnnotation = field.getAnnotation(ElementTitle.class);
                if (titleAnnotation != null) {
                    fieldsMap.put(field, titleAnnotation.value());
                } else {
                    fieldsMap.put(field, field.getName());
                }
            }

            PAGES_REPOSITORY.put((Class<? extends Page>) page, fieldsMap);
        }
    }

    private static Set<Class<?>> getAllClasses() {
        Set<Class<?>> allClasses = new HashSet();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            for (ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClassesRecursive(PROPERTIES.getPagesPackage())) {
                allClasses.add(info.load());
            }
        } catch (IOException ex) {
            LOG.warn("Failed to shape class info set", ex);
        }

        return allClasses;
    }
}
