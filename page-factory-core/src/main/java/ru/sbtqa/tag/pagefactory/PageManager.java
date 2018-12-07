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

    private static final ThreadLocal<Map<Class<? extends Page>, Map<Field, String>>> PAGES_REPOSITORY
            = ThreadLocal.withInitial(HashMap::new);
    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    private PageManager() {
    }

    public static Map<Class<? extends Page>, Map<Field, String>> getPageRepository() {
        return PAGES_REPOSITORY.get();
    }

    /**
     * Initialize page with specified title and save its instance to
     * {@link PageContext#getCurrentPage()} for further use
     *
     * @param title a page title
     * @return the page instance
     * @throws PageInitializationException if failed to execute corresponding
     * page constructor
     */
    public static Page getPage(String title) throws PageInitializationException {
        if (null == PageContext.getCurrentPage()
                || !PageContext.getCurrentPageTitle().equals(title)
                || Environment.getDriverService().isDriverEmpty()) {
            getPage(getPageClass(title));
        }
        return PageContext.getCurrentPage();
    }

    /**
     * Get Page by class
     *
     * @param pageClass a page class
     * @return the page object
     * @throws PageInitializationException if failed to execute corresponding
     * page constructor
     */
    public static Page getPage(Class<? extends Page> pageClass) throws PageInitializationException {
        Page page = bootstrapPage(pageClass);
        if (page == null) {
            throw new AutotestError("Page object with title '" + pageClass + "' is not registered");
        }
        PageContext.setCurrentPage(page);
        return page;
    }

    /**
     * Run constructor of specified page class and put its instance into static
     * {@link PageContext#getCurrentPage()} variable
     *
     * @param page a page class
     * @return the initialized page object
     * @throws PageInitializationException if failed to execute corresponding
     * page constructor
     */
    private static Page bootstrapPage(Class<?> page) throws PageInitializationException {
        if (page != null) {
            try {
                @SuppressWarnings("unchecked")
                Constructor<Page> constructor = ((Constructor<Page>) page.getConstructor());
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new PageInitializationException("Failed to initialize page '" + page + "'", e);
            }
        }
        return null;
    }

    /**
     * @param title a page title
     * @return the page class
     */
    private static Class<? extends Page> getPageClass(String title) {
        for (Map.Entry<Class<? extends Page>, Map<Field, String>> pageEntry : PAGES_REPOSITORY.get().entrySet()) {
            Class<? extends Page> page = pageEntry.getKey();
            String pageTitle = null;
            if (null != page.getAnnotation(PageEntry.class)) {
                pageTitle = page.getAnnotation(PageEntry.class).title();
            } else {
                try {
                    pageTitle = (String) FieldUtils.readStaticField(page, "title", true);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    LOG.debug("Failed to read {} because it is not page object", title, ex);
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
     * @param title a page title
     * @return the page object
     * @throws PageInitializationException if failed to execute corresponding
     * page constructor
     */
    public static Page changeUrlByTitle(String title) throws PageInitializationException {

        Class<?> pageClass = getPageClass(title);
        if (pageClass == null) {
            return null;
        }

        PageEntry annotation = pageClass.getAnnotation(PageEntry.class);
        String currentUrl = Environment.getDriverService().getDriver().getCurrentUrl();
        if (annotation != null && !annotation.url().isEmpty()) {
            if (currentUrl == null) {
                throw new AutotestError("Current URL is null");
            } else {
                try {
                    URL newUrl = new URL(currentUrl);
                    String finalUrl = new URL(newUrl.getProtocol(), newUrl.getHost(), newUrl.getPort(),
                            annotation.url()).toString();
                    Environment.getDriverService().getDriver().navigate().to(finalUrl);
                } catch (MalformedURLException ex) {
                    LOG.error("Failed to get current url", ex);
                }
            }

            Page page = bootstrapPage(pageClass);
            PageContext.setCurrentPage(page);

            return page;
        }

        throw new AutotestError("Page " + title + " doesn't have fast URL in PageEntry");
    }

    public static void cachePages() {
        for (Class<?> page : getAllClasses()) {
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

            PAGES_REPOSITORY.get().put((Class<? extends Page>) page, fieldsMap);
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
