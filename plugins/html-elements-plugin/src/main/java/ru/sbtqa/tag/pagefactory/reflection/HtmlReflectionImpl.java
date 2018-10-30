package ru.sbtqa.tag.pagefactory.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException;
import ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException;
import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

public class HtmlReflectionImpl extends ReflectionImpl implements HtmlReflection {

    private boolean isUsedBlock = false;
    private WebElement usedBlock = null;

    @Override
    public boolean isChildOf(Class<?> parent, Field field) {
        Class<?> fieldType = field.getType();
        while (fieldType != null && fieldType != Object.class) {
            if (fieldType == parent) {
                return true;
            }
            fieldType = fieldType.getSuperclass();
        }

        return false;
    }

    @Override
    public <T extends WebElement> T findElementInBlockByTitle(Page page, String blockPath, String title, Class<T> type) throws PageException {
        for (HtmlElement block : findBlocks(page, blockPath)) {
            T found = findElementInBlock(block, title, type);
            if (null != found) {
                return found;
            }
        }
        throw new ElementNotFoundException(String.format("Couldn't find element '%s' in '%s'", title, blockPath));
    }

    /**
     * Find element with required title and type inside of the given block.
     * Return null if didn't find any
     *
     * @param block block object
     * @param elementTitle value of ElementTitle annotation of required
     * element
     * @param type type of element to return
     * @param <T> any WebElement or its derivative
     * @return found element or null (exception should be thrown by a caller
     * that could no find any elements)
     */
    private <T extends WebElement> T findElementInBlock(HtmlElement block, String elementTitle, Class<T> type)
            throws ElementDescriptionException {
        for (Field f : FieldUtils.getAllFields(block.getClass())) {
            if (isRequiredElement(f, elementTitle) && type.isAssignableFrom(f.getType())) {
                f.setAccessible(true);
                try {
                    return type.cast(f.get(block));
                } catch (IllegalAccessException iae) {
                    // Since we explicitly set the field to be accessible, this exception is unexpected.
                    // It might mean that we try to get field in context of an object it doesn't belong to
                    throw new FactoryRuntimeException(
                            String.format("Internal error during attempt to find element '%s' in block '%s'",
                                    elementTitle, block.getName()), iae);
                } catch (ClassCastException cce) {
                    throw new ElementDescriptionException(
                            String.format("Element '%s' was found in block '%s', but it's type is incorrect."
                                            + "Requested '%s', but got '%s'",
                                    elementTitle, block.getName(), type.getName(), f.getType()), cce);
                }
            }
        }
        return null;
    }

    @Override
    public WebElement findElementInBlockByTitle(Page page, String blockPath, String title) throws PageException {
        return findElementInBlockByTitle(page, blockPath, title, WebElement.class);
    }

    @Override
    public <T extends WebElement> List<T> findListOfElements(Page page, String listTitle, Class<T> type) throws PageException {
        return findListOfElements(listTitle, type, page);
    }

    @Override
    public <T extends WebElement> List<T> findListOfElements(String listTitle, Class<T> type, Object context) throws PageException {
        for (Field field : FieldUtilsExt.getDeclaredFieldsWithInheritance(context.getClass())) {
            if (isRequiredElement(field, listTitle) && List.class.isAssignableFrom(field.getType())
                    && field.getGenericType() instanceof ParameterizedType
                    && ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(type)) {
                field.setAccessible(true);
                try {
                    return (List<T>) field.get(context);
                } catch (IllegalAccessException e) {
                    throw new FactoryRuntimeException(
                            String.format("Internal error during attempt to find list '%s'", listTitle), e);
                }
            }
        }
        throw new ElementNotFoundException(String.format("Couldn't find elements list '%s' on page '%s'", listTitle, PageContext.getCurrentPageTitle()));
    }

    @Override
    public List<WebElement> findListOfElements(Page page, String listTitle) throws PageException {
        return findListOfElements(page, listTitle, WebElement.class);
    }

    @Override
    public <T extends WebElement> List<T> findListOfElementsInBlock(Page page, String blockPath, String listTitle, Class<T> type) throws PageException {
        Object block = findBlock(page, blockPath);
        return findListOfElements(listTitle, type, block);
    }

    @Override
    public List<WebElement> findListOfElementsInBlock(Page page, String blockPath, String listTitle) throws PageException {
        return findListOfElementsInBlock(page, blockPath, listTitle, WebElement.class);
    }

    @Override
    public HtmlElement findBlock(Page page, String blockPath) throws NoSuchElementException {
        try {
            List<HtmlElement> blocks = findBlocks(blockPath, page, true);
            if (blocks.isEmpty()) {
                throw new java.util.NoSuchElementException(String.format("Couldn't find block '%s' on a page '%s'",
                        blockPath, PageContext.getCurrentPageTitle()));
            }
            return blocks.get(0);
        } catch (IllegalAccessException ex) {
            throw new FactoryRuntimeException(String.format("Internal error during attempt to find block '%s'", blockPath), ex);
        }
    }

    @Override
    public List<HtmlElement> findBlocks(Page page, String blockPath) throws NoSuchElementException {
        try {
            return findBlocks(blockPath, page, false);
        } catch (IllegalAccessException ex) {
            throw new FactoryRuntimeException(String.format("Internal error during attempt to find a block '%s'", blockPath), ex);
        }
    }

    /**
     * Finds blocks by required path/name in the given context. Block is a
     * class that extends HtmlElement. If blockPath contains delimiters, it
     * will be treated as a full path, and block should be located by the
     * exactly that path. Otherwise, recursive search via all blocks is
     * being performed
     *
     * @param blockPath full path or just a name of the block to search
     * @param context object where the search will be performed
     * @param returnFirstFound whether the search should be stopped on a
     * first found block (for faster searches)
     * @return list of found blocks. could be empty
     * @throws IllegalAccessException if called with invalid context
     */
    private List<HtmlElement> findBlocks(String blockPath, Object context, boolean returnFirstFound)
            throws IllegalAccessException {
        String[] blockChain;
        if (blockPath.contains("->")) {
            blockChain = blockPath.split("->");
        } else {
            blockChain = new String[]{blockPath};
        }
        List<HtmlElement> found = new ArrayList<>();
        for (Field currentField : FieldUtilsExt.getDeclaredFieldsWithInheritance(context.getClass())) {
            if (isBlockElement(currentField)) {
                if (isRequiredElement(currentField, blockChain[0])) {
                    currentField.setAccessible(true);
                    // isBlockElement() ensures that this is a HtmlElement instance
                    HtmlElement foundBlock = (HtmlElement) currentField.get(context);
                    if (blockChain.length == 1) {
                        // Found required block directly inside the context
                        found.add(foundBlock);
                        if (returnFirstFound) {
                            return found;
                        }
                    } else {
                        // Continue to search in the element chain, reducing its length by the first found element
                        // +2 because '->' adds 2 symbols
                        String reducedPath = blockPath.substring(blockChain[0].length() + 2);
                        found.addAll(findBlocks(reducedPath, foundBlock, returnFirstFound));
                    }
                } else if (blockChain.length == 1) {
                    found.addAll(findBlocks(blockPath, currentField.get(context), returnFirstFound));
                }
            }
        }
        return found;
    }

    /**
     * Check if corresponding field is a block. I.e. it has
     * {@link ElementTitle} annotation and extends {@link HtmlElement} class
     * directly
     *
     * @param field field that is being checked
     * @return true|false
     */
    private boolean isBlockElement(Field field) {
        return (null != field.getAnnotation(ElementTitle.class))
                && isChildOf(HtmlElement.class, field);
    }

    @Override
    public void executeMethodByTitleInBlock(Page page, String block, String actionTitle) throws NoSuchMethodException {
        executeMethodByTitleInBlock(page, block, actionTitle, new Object[0]);
    }

    @Override
    public void executeMethodByTitleInBlock(Page page, String blockPath, String actionTitle, Object... parameters) throws NoSuchMethodException {
        HtmlElement block = findBlock(page, blockPath);
        Method[] methods = block.getClass().getMethods();
        for (Method method : methods) {
            if (isRequiredAction(method, actionTitle)) {
                try {
                    method.setAccessible(true);
                    if (parameters == null || parameters.length == 0) {
                        MethodUtils.invokeMethod(block, method.getName());
                    } else {
                        MethodUtils.invokeMethod(block, method.getName(), parameters);
                    }
                    return;
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new FactoryRuntimeException(String.format("Failed to execute method '%s' in the following block: '%s'",
                            actionTitle, blockPath), e);
                }
            }
        }

        // TODO WTF??
        isUsedBlock = true;
        usedBlock = block;
        List<Method> methodList = getDeclaredMethods(page.getClass());
        for (Method method : methodList) {
            if (isRequiredAction(method, actionTitle)) {
                try {
                    method.setAccessible(true);
                    MethodUtils.invokeMethod(page, method.getName(), parameters);
                    return;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new FactoryRuntimeException(String.format("Failed to execute method '%s' in the following block: '%s'",
                            actionTitle, blockPath), e);
                }
            }
        }

        throw new NoSuchMethodException(String.format("There is no '%s' method in block '%s'", actionTitle, blockPath));
    }

    @Override
    public <T extends TypifiedElement> T getTypifiedElementByTitle(Page page, String title) throws PageException {
        if (!isUsedBlock) {
            for (Field field : FieldUtilsExt.getDeclaredFieldsWithInheritance(page.getClass())) {
                if (isRequiredElement(field, title) && isChildOf(TypifiedElement.class, field)) {
                    return getElementByField(page, field);
                }
            }
        } else {
            for (Field field : FieldUtilsExt.getDeclaredFieldsWithInheritance(usedBlock)) {
                if (isRequiredElementInBlock(field, title)) {
                    return getElementByField(usedBlock, field);
                }
            }
        }
        throw new ElementNotFoundException(String.format("Element '%s' is not present on current page '%s''", title, PageContext.getCurrentPageTitle()));
    }

    /**
     * Check whether {@link Name} annotation of the field has a
     * required value
     *
     * @param field field to check
     * @param title value of ElementTitle annotation of required element
     * @return true|false
     */
    private static boolean isRequiredElementInBlock(Field field, String title) {
        return getFieldTitleInBlock(field).equals(title);
    }

    /**
     * Return value of {@link Name} annotation for the field. If
     * none present, return empty string
     *
     * @param field field to check
     * @return either an element title, or an empty string
     */
    private static String getFieldTitleInBlock(Field field) {
        for (Annotation a : field.getAnnotations()) {
            if (a instanceof Name) {
                return ((Name) a).value();
            }
        }
        return "";
    }

    @Override
    public <T> T getElementByField(Object parentObject, Field field) throws ElementDescriptionException {
        Object element = super.getElementByField(parentObject, field);
        isUsedBlock = false;
        usedBlock = null;

        return (T) element;
    }
}
