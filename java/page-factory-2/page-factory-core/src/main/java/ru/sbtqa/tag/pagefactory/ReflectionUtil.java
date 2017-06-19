package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException;
import ru.sbtqa.tag.pagefactory.util.PageFactoryUtils;

import java.lang.reflect.Field;
import java.util.Map;

import static ru.sbtqa.tag.pagefactory.util.PageFactoryUtils.getElementByField;

/**
 * Common reflection utils as static methods from Page.Core
 *
 * May be it should be in entry_point module
 *
 */
public class ReflectionUtil {
    
    public static final Logger LOG = LoggerFactory.getLogger(ReflectionUtil.class);
    
    /**
     * Search for the given WebElement in page repository storage, that is being
     * generated during preconditions to all tests. If element is found, return
     * its title annotation. If nothing found, log debug message and return
     * toString() of corresponding element
     *
     * @param element WebElement to search
     * @return title of the given element
     */
    public static String getElementTitle(Page page, WebElement element) {
        for (Map.Entry<Field, String> entry : PageFactory.getPageRepository().get(page.getClass()).entrySet()) {
            try {
                if (getElementByField(page, entry.getKey()) == element) {
                    return entry.getValue();
                }
            } catch (java.util.NoSuchElementException | StaleElementReferenceException | ElementDescriptionException ex) {
                LOG.debug("Failed to get element '" + element + "' title", ex);
            }
        }
        return element.toString();
    }
    
    /**
     * Get title annotation of specified WebElement, and add it as a
     * parameter to allure report results, with corresponding value. If
     * there is no title annotation, log warning and exit
     *
     * @param webElement WebElement to add
     * @param text value for the specified element
     */
    public static void addToReport(WebElement webElement, String text) {
        String elementTitle = getElementTitle(PageContext.getCurrentPage(), webElement);
        PageFactoryUtils.addToReport(elementTitle, text);
    }
    
    
    
    //TODO REMOVE REDIRECT
//    /**
//     * Search for the given given element among the parent object fields,
//     * check whether it has a {@link
//     * RedirectsTo} annotation, and return a redirection page class, if so.
//     * Search goes in recursion if it meets HtmlElement field, as given
//     * element could be inside of the block
//     *
//     * @param element element that is being checked for redirection
//     * @param parent parent object
//     * @return class of the page, this element redirects to
//     */
//    public static Class<? extends Page> findRedirect(Object parent, Object element) {
//        List<Field> fields = FieldUtilsExt.getDeclaredFieldsWithInheritance(parent.getClass());
//
//        for (Field field : fields) {
//            RedirectsTo redirect = field.getAnnotation(RedirectsTo.class);
//            if (redirect != null) {
//                try {
//                    field.setAccessible(true);
//                    Object targetField = field.get(parent);
//                    if (targetField != null) {
//                        if (targetField == element) {
//                            return redirect.page();
//                        }
//                    }
//                } catch (NoSuchElementException | StaleElementReferenceException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
//                    LOG.debug("Failed to get page destination to redirect for element", ex);
//                }
//            }
//            // ПЛОХО завязано
//            if (isChildOf(HtmlElement.class, field)) {
//                field.setAccessible(true);
//                Class<? extends Page> redirects = null;
//                try {
//                    redirects = findRedirect(field.get(parent), element);
//                } catch (IllegalArgumentException | IllegalAccessException ex) {
//                    LOG.debug("Failed to get page destination to redirect for html element", ex);
//                }
//                if (redirects != null) {
//                    return redirects;
//                }
//            }
//        }
//        return null;
//    }
    
//
//    ХЗ пока куда это
    
    //TODO REMOVE REDIRECT
//    /**
//     * Return class for redirect if annotation contains and null if not present
//     *
//     * @param element element, redirect for which is being searched
//     * @return class of the page object, element redirects to
//     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException
//     * if failed to find redirect
//     */
//    // TODO there is control current page logic
//    public static Class<? extends Page> getElementRedirect(WebElement element) throws ElementDescriptionException {
//        try {
//            Page currentPage = PageContext.getCurrentPage();
//            if (null == currentPage) {
//                LOG.warn("Current page not initialized yet. You must initialize it by hands at first time only.");
//                return null;
//            }
//            return findRedirect(currentPage, element);
//        } catch (IllegalArgumentException | PageInitializationException ex) {
//            throw new ElementDescriptionException("Failed to get element redirect", ex);
//        }
//    }
}
 

