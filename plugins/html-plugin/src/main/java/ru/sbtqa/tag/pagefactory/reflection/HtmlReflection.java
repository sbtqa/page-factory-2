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
import java.util.stream.Collectors;

public class HtmlReflection extends DefaultReflection {

    private boolean isUsedBlock = false;
    private WebElement usedBlock = null;

    /**
     * Check whether given field is a child of specified class
     *
     * @param parent class that is supposed to be parent
     * @param field field to check
     * @return true|false
     */
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

    /**
     * Find element of required type in the specified block, or block chain on
     * the page. If blockPath is separated by &gt; delimiters, it will be
     * treated as a block path, so element could be found only in the described
     * chain of blocks. Otherwise, given block will be searched recursively on
     * the page
     *
     * @param <T> supposed type of the field. if field cannot be cast into
     * this type, it will fail
     * @param page the page on which the method will be executed
     * @param blockPath block or block chain where element will be searched
     * @param title value of ElementTitle annotation of required element
     * @param type type of the searched element
     * @return web element of the required type
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException if
     * element was not found
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException if element was not found, but with the wrong type
     */
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

    /**
     * Acts exactly like
     * {@link HtmlReflection#findElementInBlockByTitle(Page, String, String, Class)}, but return a
     * WebElement instance. It still could be casted to HtmlElement and
     * TypifiedElement any class that extend them.
     *
     * @param page the page on which the method will be executed
     * @param blockPath block or block chain where element will be searched
     * @param title value of ElementTitle annotation of required element
     * @return WebElement
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException if
     * element was not found
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException if element was not found, but with the wrong type
     */
    public WebElement findElementInBlockByTitle(Page page, String blockPath, String title) throws PageException {
        return findElementInBlockByTitle(page, blockPath, title, WebElement.class);
    }

    /**
     * Finds elements list in context of current page See
     * ${@link HtmlReflection#findListOfElements(String, Class, Object)}  for detailed
     * description
     *
     * @param page the page on which the method will be executed
     * @param listTitle value of ElementTitle annotation of required element
     * @param type type of elements in list that is being searched for
     * @param <T> type of elements in returned list
     * @return list of elements of particular type
     * @throws PageException if nothing found or current page is not initialized
     */
    public <T extends WebElement> List<T> findListOfElements(Page page, String listTitle, Class<T> type) throws PageException {
        return findListOfElements(listTitle, type, page);
    }

    /**
     * Find list of elements of the specified type with required title in
     * the given context. Context is either a page object itself, or a block
     * on the page. !BEWARE! field.get() will actually query browser to
     * evaluate the list, so this method might reduce performance!
     *
     * @param listTitle value of ElementTitle annotation of required element
     * @param type type of elements inside of the list
     * @param context object where search should be performed
     * @param <T> type of elements in returned list
     * @return list of WebElement's or its derivatives
     * @throws PageException if didn't find any list or current page wasn't
     * initialized
     */
    @SuppressWarnings("unchecked")
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

    /**
     * Finds elements list in context of current page See
     * ${@link HtmlReflection#findListOfElements(String, Class, Object)} for detailed
     * description
     *
     * @param page the page on which the method will be executed
     * @param listTitle value of ElementTitle annotation of required element
     * @return list of WebElement's
     * @throws PageException if nothing found or current page is not initialized
     */
    public List<WebElement> findListOfElements(Page page, String listTitle) throws PageException {
        return findListOfElements(page, listTitle, WebElement.class);
    }

    /**
     * Find elements list in context of required block See
     * ${@link HtmlReflection#findListOfElements(String, Class, Object)} for detailed
     * description
     *
     * @param page the page on which the method will be executed
     * @param blockPath full path or just a name of the block to search
     * @param listTitle value of ElementTitle annotation of required element
     * @param type type of elements in list that is being searched for
     * @param <T> type of elements in returned list
     * @return list of elements of particular type
     * @throws PageException if nothing found or current page is not initialized
     */
    public <T extends WebElement> List<T> findListOfElementsInBlock(Page page, String blockPath, String listTitle, Class<T> type) throws PageException {
        Object block = findBlock(page, blockPath);
        return findListOfElements(listTitle, type, block);
    }

    /**
     * Finds elements list in context of required block See
     * ${@link HtmlReflection#findListOfElements(String, Class, Object)} for detailed
     * description
     *
     * @param page the page on which the method will be executed
     * @param blockPath full path or just a name of the block to search
     * @param listTitle value of ElementTitle annotation of required element
     * @return list of WebElement's
     * @throws PageException if nothing found or current page is not initialized
     */
    public List<WebElement> findListOfElementsInBlock(Page page, String blockPath, String listTitle) throws PageException {
        return findListOfElementsInBlock(page, blockPath, listTitle, WebElement.class);
    }

    /**
     * See {@link HtmlReflection#findBlocks(String, Object, boolean)} for description.
     * This wrapper finds only one block. Search is being performed on a current
     * page
     *
     * @param page the page on which the method will be executed
     * @param blockPath full path or just a name of the block to search
     * @return first found block object
     * @throws java.util.NoSuchElementException if couldn't find any block
     */
    public HtmlElement findBlock(Page page, String blockPath) {
        List<HtmlElement> blocks = findBlocks(blockPath, page, true);
        if (blocks.isEmpty()) {
            throw new java.util.NoSuchElementException(String.format("Couldn't find block '%s' on a page '%s'",
                    blockPath, PageContext.getCurrentPageTitle()));
        }
        return blocks.get(0);
    }

    /**
     * See {@link HtmlReflection#findBlocks(String, Object, boolean)} for description.
     * Search is being performed on a current page
     *
     * @param page the page on which the method will be executed
     * @param blockPath full path or just a name of the block to search
     * @return list of objects that were found by specified path
     */
    public List<HtmlElement> findBlocks(Page page, String blockPath) {
        return findBlocks(blockPath, page, false);
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
     */
    //TODO - покрыть юнит тестами - логика базовая и не супер очевидная
    private List<HtmlElement> findBlocks(String blockPath, Object context, boolean returnFirstFound) {
        String[] blockChain;
        if (blockPath.contains("->")) {
            blockChain = blockPath.split("->");
        } else {
            blockChain = new String[]{blockPath};
        }
        List<HtmlElement> found = new ArrayList<>();

        //ищем по глубине - чем менее глубоко нашли - тем раньше должны вернуть
        List<Field> blockElements = FieldUtilsExt.getDeclaredFieldsWithInheritance(context.getClass()).stream()
                .filter(this::isBlockElement)
                .collect(Collectors.toList());

        for (Field currentField : blockElements) {
            if (isRequiredElement(currentField, blockChain[0])) {//нашли нужный элемент по имени
                currentField.setAccessible(true);
                // isBlockElement() ensures that this is a HtmlElement instance
                HtmlElement foundBlock = (HtmlElement) getNextContext(currentField, context, blockPath);
                if (blockChain.length == 1) {//если это последний элемент для поиска
                    // Found required block directly inside the context
                    found.add(foundBlock);
                    if (returnFirstFound) {
                        return found;
                    }
                } else {//мы в середине цепочки - идём глубже
                    // Continue to search in the element chain, reducing its length by the first found element
                    // +2 because '->' adds 2 symbols
                    String reducedPath = blockPath.substring(blockChain[0].length() + 2);
                    found.addAll(findBlocks(reducedPath, foundBlock, returnFirstFound));
                }
            }
        }

        //не нашли совпадения по имени - значит ищем по имени во всех блоках внутри контекста
        if (blockChain.length == 1) {//TODO - тут на самом деле ещё бага - в случае, когда A->B->C , мы С ищем во всех подблоках B
            for (Field currentField : blockElements) {
                if (!isRequiredElement(currentField, blockChain[0])) {
                    found.addAll(findBlocks(blockPath, getNextContext(currentField, context, blockPath), returnFirstFound));
                }
            }
        }


        return found;
    }

    private Object getNextContext(Field field, Object context, String blockPath) {
        try {
            field.setAccessible(true);
            return field.get(context);
        } catch (IllegalAccessException e) {
            throw new FactoryRuntimeException(String.format("Internal error during attempt to find a block '%s'", blockPath), e);
        }
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

    /**
     * Execute parameter-less method inside of the given block element.
     *
     * @param page the page on which the method will be executed
     * @param block block title, or a block chain string separated with '-&gt;'
     * symbols
     * @param actionTitle title of the action to execute
     * @throws NoSuchMethodException if required method couldn't be
     * found
     */
    public void executeMethodByTitleInBlock(Page page, String block, String actionTitle) throws NoSuchMethodException {
        executeMethodByTitleInBlock(page, block, actionTitle, new Object[0]);
    }

    /**
     * Execute method with one or more parameters inside of the given block
     * element !BEWARE! If there are several elements found by specified block
     * path, a first one will be used!
     *
     * @param page the page on which the method will be executed
     * @param blockPath block title, or a block chain string separated with
     * '-&gt;' symbols
     * @param actionTitle title of the action to execute
     * @param parameters parameters that will be passed to method
     * @throws NoSuchMethodException if required method couldn't be
     * found
     */
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

    /**
     * Find specified TypifiedElement by title annotation among current page
     * fields
     *
     * @param <T> supposed type of the field. if field cannot be cast into this type, it will fail
     * @param page the page on which the method will be executed
     * @param title a {@link String} object.
     * @return a {@link org.openqa.selenium.WebElement} object.
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if nothing found or current page is not initialized
     */
    @SuppressWarnings(value = "unchecked")
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
