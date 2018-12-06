package ru.sbtqa.tag.pagefactory.reflection;

import static java.lang.String.format;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import ru.sbtqa.tag.datajack.TestDataProvider;
import ru.sbtqa.tag.datajack.exceptions.DataException;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.utils.Wait;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.sbtqa.tag.qautils.properties.Props;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;
import ru.yandex.qatools.htmlelements.utils.HtmlElementUtils;

public class HtmlReflectionNEW extends DefaultReflection {
    public static <T extends WebElement> T find(String name) {
        return find(name, true);
    }

    public static <T extends WebElement> T find(String name, boolean wait) {
        return find(null, name, wait);
    }

    public static <T extends WebElement> T find(T context, String name, boolean wait) {
        ComplexElement element = findComplexElement(context, name, wait);
        WebElement currentElement = element.getElement();

        if (currentElement == null) {
            String errorText = format("Элемент не был найден на странице: %s", name);
            if (!(wait || element.isPresent())) {
                throw new IllegalStateException(errorText);
            } else {
                throw new AutotestError(errorText);
            }
        }

        if (wait) {
            Wait.visibility(ElementUtils.getWebElement(element.getElement()),
                    "На странице не отображается элемент " + formErrorMessage(element));
        }

        currentPageMatchesWebPage(element);

        if (currentElement.getClass().equals(RemoteWebElement.class)) {
            Class type = findType(currentElement);
            element.setElement(createElement(type, currentElement));
        }
        return (T) element.getElement();
    }

    public static <T extends WebElement> List<T> findList(T context, String name) {
        Field field;
        ComplexElement element = new ComplexElement(context, name, false);

        if (name.contains(ComplexElement.ELEMENT_SEPARATOR)) {
            int lastSeparatorIndex = name.lastIndexOf(ComplexElement.ELEMENT_SEPARATOR);
            element = findComplexElement(context, name.substring(0, lastSeparatorIndex), false);
            String listName = name.substring(lastSeparatorIndex + 2, name.length());

            field = getField(element.getElement(), listName);
            if (field == null) {
                return (List<T>) TestIdUtils.findListWithEmptyResult(element.getElement(), listName);
            }
        } else {
            field = getField(context, name);
        }

        WebElement currentElement = element.getElement();
        try {
            Object fieldObject = field.get(currentElement == null ? PageContext.getCurrentPage() : currentElement);

            if (!field.getType().isAssignableFrom(List.class)) {
                throw new AutotestError(format("Элемент был найден, но он не является списком. Поиск выполнялся по пути: %s", name));
            }
            return (List<T>) fieldObject;
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new AutotestError(format("Ошибка при поиске списка по пути: %s", name), ex);
        }

    }

    private static <T extends WebElement> ComplexElement findComplexElement(T context, String name, boolean wait) {
        ComplexElement element = new ComplexElement(context, name, wait);
        try {
            for (; element.getCurrentPosition() < element.getElementPath().size();
                    element.setCurrentPosition(element.getCurrentPosition() + 1)) {
                Field field = getField(element.getElement(), element.getCurrentName());

                if (field == null) {
                    findByTestId(element);
                    break;
                }
                findElement(field, element);
            }
            if (element.getCurrentPosition() > 0) {
                element.setCurrentPosition(element.getCurrentPosition() - 1);
            }
        } catch (AutotestError | IllegalArgumentException | IllegalAccessException ex) {
            throw new AutotestError("На странице не найден элемент " + formErrorMessage(element), ex);
        }
        return element;
    }

    private static void currentPageMatchesWebPage(ComplexElement element) {
        if (Boolean.valueOf(Props.get("verify.page.before.action", "false"))) {
            LOG.info("Будет выполнена проверка соответствия страницы для элемента: " + formErrorMessage(element));
            PageUtils.verifyPageByDataTestId();
        }
    }

    private static String formErrorMessage(ComplexElement element) {
        StringBuilder errorMessage = new StringBuilder();
        String elementOflistNotFound = "";
        String currenElementName = element.getCurrentName();

        if (element.getElementPath().size() > 1) {
            if (currenElementName.chars().allMatch(Character::isDigit)) {
                String prevElement = element.getElementPath().get(element.getCurrentPosition() - 1).toString();
                elementOflistNotFound = format("с номером \"%s\" в списке: %s", currenElementName, prevElement);
            }
            if (elementOflistNotFound.isEmpty()) {
                errorMessage.append("с именем: ").append(currenElementName);
            } else {
                errorMessage.append(elementOflistNotFound);
            }
            errorMessage.append(". Поиск выполнялся по пути: ").append(element.getFullElementPath());
        } else {
            errorMessage.append("с именем: ").append(currenElementName);
        }
        return errorMessage.toString();
    }

    public static <T extends WebElement> T find(String name, Class<T> type) {
        return find(name, type, true);
    }

    public static <T extends WebElement> T find(String name, Class<T> type, boolean wait) {
        Class instanсeType = type;
        T element = find(name, wait);
        Class elementType = element.getClass();

        if (element instanceof TypifiedElement) {
            if (instanсeType.isAssignableFrom(elementType)) {
                return (T) element;
            } else {
                throw new AutotestError(format("Найден ЕФС компонент с именем \"%s\". Но его тип не соотвествует искомому. "
                        + "Ожидался: %s. Найден: %s", name, type.getName(), elementType.getName()));
            }
        }
    }

    public static <T extends WebElement> T find(String name, List<Class> clazz) {
        return find(name, clazz, true);
    }

    public static <T extends WebElement> T find(String name, List<Class> clazz, boolean wait) {
        T element = find(name, wait);
        if (!clazz.contains(element.getClass())) {
            throw new AutotestError(format("Некоррекный тип элемента. Имя элемента: %s. Допустимые типы: %s", name, clazz.toString()));
        }
        return element;
    }

    private static void findElement(Field field, ComplexElement element) throws IllegalAccessException {
        WebElement currentElement = element.getElement();
        Object fieldObject = field.get(currentElement == null ? PageContext.getCurrentPage() : currentElement);
        if (field.getType().isAssignableFrom(List.class)) {
            findElementOfList((List<WebElement>) fieldObject, element);
        } else {
            element.setElement((WebElement) fieldObject);
        }
    }

    private static Field getField(Object element, String elementName) {
        Page page = PageContext.getCurrentPage();
        Class clazz = element == null ? page.getClass() : element.getClass();

        for (Field field : FieldUtilsExt.getFieldsListWithAnnotation(clazz, ElementTitle.class)) {
            field.setAccessible(true);
            if (field.getAnnotation(ElementTitle.class).value().equals(elementName)) {
                return field;
            }
        }
        return null;
    }

    private static void findElementOfList(List<WebElement> list, ComplexElement currentElement) {
        int position = currentElement.getCurrentPosition();

        int index = 0;
        List<String> elementPath = currentElement.getElementPath();

        if (position + 1 < elementPath.size()) {
            String nextParam = elementPath.get(position + 1);
            boolean isNumeric = nextParam.chars().allMatch(Character::isDigit);
            if (isNumeric) {
                index = Integer.parseInt(elementPath.get(position + 1)) - 1;
                currentElement.setCurrentPosition(position + 1);
            } else {
                LOG.info("Следующий за списком элемент не является индексом. Индекс элемента списка: 0.");
            }
        }
        if (index >= list.size()) {
            boolean isLastElement = currentElement.getCurrentPosition() + 1 != currentElement.getElementPath().size();
            if (!isLastElement && currentElement.isWaitAppear()) {
                throw new AutotestError(format("Индекс элемента в списке "
                        + "превышает размер списка. Индекс: %s. Размер: %s", index, list.size()));
            } else {
                currentElement.setElement(null);
            }
        } else {
            LOG.debug("Элемент спика с индексом " + index + " был получен.");
            currentElement.setElement(list.get(index));
        }
    }

    private static <T extends WebElement> T createElement(Class<T> type, WebElement element) {
        try {
            return (T) HtmlElementUtils.newInstance(type, element);
        } catch (NoSuchMethodException e) {
            LOG.debug("Не удалось задать тип для элемента.", e);
            return (T) element;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            throw new AutotestError("Ошибка при создании инстанса веб элемента.", ex);
        }
    }
   
}
