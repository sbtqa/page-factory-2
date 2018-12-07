package ru.sbtqa.tag.pagefactory;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
    private static final Map<Class<? extends Page>, Map<Field, String>> PAGES_REPOSITORY = new HashMap<>();
    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    private PageManager() {}

    public static Map<Class<? extends Page>, Map<Field, String>> getPageRepository() {
        return PAGES_REPOSITORY;
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
     * @throws PageInitializationException if failed to execute corresponding page constructor
     */
    public static Page getPage(Class<? extends Page> pageClass) throws PageInitializationException {
        Page page = bootstrapPage(pageClass);
        if (page == null) {
            throw new AutotestError("Page object with title '" + pageClass + "' is not registered");
        }
        PageContext.setCurrentPage(page);
        setUrl(page.getUrl());

        return page;
    }

    /**
     * Run constructor of specified page class and put its instance into static
     * {@link PageContext#currentPage} variable
     *
     * @param page a page class
     * @return the initialized page object
     * @throws PageInitializationException if failed to execute corresponding page constructor
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

    private static Class<? extends Page> getPageClass(String title) {
        for (Map.Entry<Class<? extends Page>, Map<Field, String>> pageEntry : PAGES_REPOSITORY.entrySet()) {
            Class<? extends Page> page = pageEntry.getKey();
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

    private static void setUrl(String url) {
        if (!url.isEmpty()) {
            Environment.getDriverService().getDriver().get(url);
        }
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
