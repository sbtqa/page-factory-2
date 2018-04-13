package ru.sbtqa.tag.pagefactory;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.WebElement;
import org.reflections.Reflections;
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

    private static String pagesPackage;
    private static PageManager pageManager;

    public PageManager(String pagesPackage) {
        this.pagesPackage = pagesPackage;
    }

    public static PageManager getInstance() {
        if (null == pageManager) {
            pageManager = new PageManager(PROPERTIES.getPagesPackage());
        }
        return pageManager;
    }

    public static Map<Class<? extends Page>, Map<Field, String>> getPageRepository() {
        return PAGES_REPOSITORY;
    }

    /**
     * Initialize page by class
     *
     * @param page TODO
     * @return TODO
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException
     * TODO
     */
    public Page getPage(Class<? extends Page> page) throws PageInitializationException {
        return bootstrapPage(page);
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
    public Page getPage(String packageName, String title) throws PageInitializationException {
        return bootstrapPage(getPageClass(packageName, title));
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
                getPage(pagesPackage, title);
            }
            if (null == PageContext.getCurrentPage()) {
                getPage(pagesPackage, title);
            }
            if (null == PageContext.getCurrentPage()) {
                throw new AutotestError("WebElementsPage object with title '" + title + "' is not registered");
            }
        }
        return PageContext.getCurrentPage();
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
    private static Page bootstrapPage(Class<?> page) throws PageInitializationException {
        if (page != null) {
            try {
                @SuppressWarnings("unchecked")
                Constructor<Page> constructor = ((Constructor<Page>) page.getConstructor());
                constructor.setAccessible(true);
                Page currentPage = constructor.newInstance();
                PageContext.setCurrentPage(currentPage);
                return currentPage;
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new PageInitializationException("Failed to initialize page '" + page + "'", e);
            }
        }
        return null;
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
        String currentUrl = Environment.getDriverService().getDriver().getCurrentUrl();
        if (annotation != null && !((PageEntry) annotation).url().isEmpty()) {
            if (currentUrl == null) {
                throw new AutotestError("Current URL is null");
            } else {
                try {
                    URL newUrl = new URL(currentUrl);
                    String finalUrl = new URL(newUrl.getProtocol(), newUrl.getHost(), newUrl.getPort(),
                            ((PageEntry) annotation).url()).toString();
                    Environment.getDriverService().getDriver().navigate().to(finalUrl);
                } catch (MalformedURLException ex) {
                    LOG.error("Failed to get current url", ex);
                }
            }

            return bootstrapPage(pageClass);
        }

        throw new AutotestError("WebElementsPage " + title + " doesn't have fast URL in PageEntry");
    }

    public void cachePages() {
        Set<Class<?>> allClasses = new HashSet();
        allClasses.addAll(getAllClasses());

        for (Class<?> page : allClasses) {
            List<Field> fields = FieldUtilsExt.getDeclaredFieldsWithInheritance(page);
            Map<Field, String> fieldsMap = new HashMap<>();
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                if (fieldType.equals(WebElement.class)) {

                    ElementTitle titleAnnotation = field.getAnnotation(ElementTitle.class);
                    if (titleAnnotation != null) {
                        fieldsMap.put(field, titleAnnotation.value());
                    } else {
                        fieldsMap.put(field, field.getName());
                    }
                }
            }

            PAGES_REPOSITORY.put((Class<? extends Page>) page, fieldsMap);
        }
    }

    private Set<Class<?>> getAllClasses() {
        Set<Class<?>> allClasses = new HashSet();

        Reflections reflections = new Reflections(PROPERTIES.getPagesPackage());
        Collection<String> allClassesString = reflections.getStore().get("SubTypesScanner").values();

        for (String clazz : allClassesString) {
            try {
                allClasses.add(Class.forName(clazz));
            } catch (ClassNotFoundException e) {
                LOG.warn("Cannot add to cache class with name {}", clazz, e);
            }
        }

        return allClasses;
    }
}
