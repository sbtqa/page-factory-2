package ru.sbtqa.tag.pagefactory.reflection;

import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Tag;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.openqa.selenium.StaleElementReferenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitles;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.ValidationRule;
import ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException;
import ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException;
import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;

public class DefaultReflection implements Reflection {

    public static final Logger LOG = LoggerFactory.getLogger(DefaultReflection.class);

    @Override
    public String getElementTitle(Page page, Object element) {
        for (Map.Entry<Field, String> entry : PageManager.getPageRepository().get(page.getClass()).entrySet()) {
            try {
                if (getElementByField(page, entry.getKey()) == element) {
                    ElementTitle elementTitle = entry.getKey().getAnnotation(ElementTitle.class);
                    if (elementTitle != null && !elementTitle.value().isEmpty()) {
                        return elementTitle.value();
                    }
                    return entry.getValue();
                }
            } catch (java.util.NoSuchElementException | StaleElementReferenceException | ElementDescriptionException ex) {
                LOG.debug("Failed to get element '" + element + "' title", ex);
            }
        }
        return element.toString();
    }

    @Override
    public void executeMethodByTitle(Page page, String title, Object... param) throws NoSuchMethodException {
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

    @Override
    public List<Method> getDeclaredMethods(Class clazz) {
        List<Method> methods = new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods()));

        Class supp = clazz.getSuperclass();

        while (supp != java.lang.Object.class) {
            methods.addAll(Arrays.asList(supp.getDeclaredMethods()));
            supp = supp.getSuperclass();
        }

        return methods;
    }

    @Override
    public Boolean isRequiredAction(Method method, String title) {
        ActionTitle actionTitle = method.getAnnotation(ActionTitle.class);
        ActionTitles actionTitles = method.getAnnotation(ActionTitles.class);
        List<ActionTitle> actionList = new ArrayList<>();

        if (actionTitles != null) {
            actionList.addAll(Arrays.asList(actionTitles.value()));
        }
        if (actionTitle != null) {
            actionList.add(actionTitle);
        }

        for (ActionTitle action : actionList) {
            if (action.value().equals(title)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public <T> T getElementByTitle(Page page, String title) throws PageException {
        for (Field field : FieldUtilsExt.getDeclaredFieldsWithInheritance(page.getClass())) {
            if (isRequiredElement(field, title)) {
                return getElementByField(page, field);
            }
        }

        throw new ElementNotFoundException(String.format("Element '%s' is not present on current page '%s''", title, page.getTitle()));
    }

    @Override
    public boolean isRequiredElement(Field field, String title) {
        return getFieldTitle(field).equals(title);
    }

    @Override
    public String getFieldTitle(Field field) {
        for (Annotation a : field.getAnnotations()) {
            if (a instanceof ElementTitle) {
                return ((ElementTitle) a).value();
            }
        }
        return "";
    }

    @Override
    public <T> T getElementByField(Object parentObject, Field field) throws ElementDescriptionException {
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

    @Override
    public void fireValidationRule(Page page, String title, Object... params) throws PageException {
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

    @Override
    public List<Tag> getScenarioTags(ScenarioDefinition scenarioDefinition) {
        try {
            return (List<Tag>) FieldUtils.readField(scenarioDefinition, "tags", true);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return new ArrayList<>();
        }
    }
}
