package ru.sbtqa.tag.pagefactory.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitles;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.ValidationRule;
import ru.sbtqa.tag.pagefactory.context.ScenarioContext;
import ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException;
import ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException;
import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.qautils.i18n.I18N;
import ru.sbtqa.tag.qautils.i18n.I18NRuntimeException;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;

public class PageFactoryUtils {
    
    public static final Logger LOG = LoggerFactory.getLogger(PageFactoryUtils.class);

    /**
     * Find method with corresponding title on current page, and execute it
     *
     * @param page the page on which the method is executing
     * @param title title of the method to call
     * @param param parameters that will be passed to method
     * @throws java.lang.NoSuchMethodException if required method couldn't be
     * found
     */
    public static void executeMethodByTitle(Page page, String title, Object... param) throws NoSuchMethodException {
        List<Method> methods = getDeclaredMethods(page.getClass());
        for (Method method : methods) {
            if (isRequiredAction(method, title)) {
                try {
                    method.setAccessible(true);
                    MethodUtils.invokeMethod(page, method.getName(), param);
                    return;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new FactoryRuntimeException("Error while executing action '" + title + "' on " + method.getDeclaringClass().getSimpleName() + " . See the caused exception below", ExceptionUtils.getRootCause(e)); 
                }
            }
        }
        
        throw new NoSuchMethodException("There is no '" + title + "' method on '" + page.getTitle() + "' page object");
    }
    
    /**
     * Return a list of methods declared tin the given class and its super
     * classes
     *
     * @param clazz class to check
     * @return list of methods. could be empty list
     */
    public static List<Method> getDeclaredMethods(Class clazz) {
        List<Method> methods = new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods()));
        
        Class supp = clazz.getSuperclass();
        
        while (supp != java.lang.Object.class) {
            methods.addAll(Arrays.asList(supp.getDeclaredMethods()));
            supp = supp.getSuperclass();
        }
        
        return methods;
    }
    
    /**
     * Check whether given method has {@link ActionTitle} or
     * {@link ActionTitles} annotation with required title
     *
     * @param method method to check
     * @param title required title
     * @return true|false
     */
    public static Boolean isRequiredAction(Method method, final String title) {
        ActionTitle actionTitle = method.getAnnotation(ActionTitle.class);
        ActionTitles actionTitles = method.getAnnotation(ActionTitles.class);
        List<ActionTitle> actionList = new ArrayList<>();
        
        if (actionTitles != null) {
            actionList.addAll(Arrays.asList(actionTitles.value()));
        }
        if (actionTitle != null) {
            actionList.add(actionTitle);
        }

        I18N i18n = I18N.getI18n(method.getClass(), ScenarioContext.getScenario());
        for (ActionTitle action : actionList) {
            String actionValue = action.value();
            try {
                actionValue = i18n.get(action.value());
            } catch (I18NRuntimeException e) {
                LOG.debug("There is no bundle for translation class. Leave it as is", e);
            }
            
            if (actionValue.equals(title)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Find specified WebElement by title annotation among current page fields
     *
     * @param page the page on which the method is executing
     * @param title title of the element to search
     * @return WebElement found by corresponding title
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if failed to
     * find corresponding element or element type is set incorrectly
     */
    public static WebElement getElementByTitle(Page page, String title) throws PageException {
        for (Field field : FieldUtilsExt.getDeclaredFieldsWithInheritance(page.getClass())) {
            if (isRequiredElement(field, title)) {
                return getElementByField(page, field);
            }
        }
        
        throw new ElementNotFoundException(String.format("Element '%s' is not present on current page '%s''", title, page.getTitle()));
    }
    
        
    /**
     * Check whether {@link ElementTitle} annotation of the field has a
     * required value
     *
     * @param field field to check
     * @param title value of ElementTitle annotation of required element
     * @return true|false
     */
    public static boolean isRequiredElement(Field field, String title) {
        return getFieldTitle(field).equals(title);
    }
    
    /**
     * Return value of {@link ElementTitle} annotation for the field. If
     * none present, return empty string
     *
     * @param field field to check
     * @return either an element title, or an empty string
     */
    public static String getFieldTitle(Field field) {
        for (Annotation a : field.getAnnotations()) {
            if (a instanceof ElementTitle) {
                return ((ElementTitle) a).value();
            }
        }
        return "";
    }
    
    /**
     * Get object from a field of specified parent
     *
     * @param parentObject object that contains(must contain) given field
     * @param field field to get
     * @param <T> supposed type of the field. if field cannot be cast into
     * this type, it will fail
     * @return element of requested type
     * @throws ElementDescriptionException in case if field does not belong
     * to the object, or element could not be cast to specified type
     */
    @SuppressWarnings("unchecked")
    public static <T> T getElementByField(Object parentObject, Field field) throws ElementDescriptionException {
        field.setAccessible(true);
        Object element;
        try {
            element = field.get(parentObject);
            return (T) element;
        } catch (IllegalArgumentException | IllegalAccessException iae) {
            throw new ElementDescriptionException("Specified parent object is not an instance of the class or "
                    + "interface, declaring the underlying field: '" + field + "'", iae);
        } catch (ClassCastException cce) {
            throw new ElementDescriptionException("Requested type is incompatible with field '" + field.getName()
                    + "' of '" + parentObject.getClass().getCanonicalName() + "'", cce);
        }
    }

    /**
     * Find a method with {@link ValidationRule} annotation on current page, and
     * call it
     *
     * @param page the page on which the method is executing
     * @param title title of the validation rule
     * @param params parameters passed to called method
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if couldn't
     * find corresponding validation rule
     */
    public static void fireValidationRule(Page page, String title, Object... params) throws PageException {
        Method[] methods = page.getClass().getMethods();
        for (Method method : methods) {
            if (null != method.getAnnotation(ValidationRule.class)
                    && method.getAnnotation(ValidationRule.class).title().equals(title)) {
                try {
                    method.invoke(page, params);
                } catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException e) {
                    LOG.debug("Failed to invoke method {}", method, e);
                    throw new FactoryRuntimeException("Failed to invoke method", e);
                }
                return;
            }
        }
        throw new PageException("There is no '" + title + "' validation rule in '" + page.getTitle() + "' page.");
    }
}
