package ru.sbtqa.tag.pagefactory.reflection;

import static java.lang.String.format;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.html.loader.CustomHtmlElementLoader;
import ru.sbtqa.tag.pagefactory.html.loader.decorators.CustomHtmlElementDecorator;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

public class ElementUtils {

    private static final String TEXT_XPATH = ".//*[text()]";

    /**
     * Смотрит, является ли значение ключем стэша, если да, то возвращает
     * значение из стэша, если нет, то само значение
     *
     * @param name значение для поиска
     * @return значение из стэша либо то же самое значение
     */
    public static String getValue(String name) {
        return Stash.getValue(name) != null ? Stash.getValue(name).toString() : name;
    }

    /**
     * Выполняет двойной клик по элементу
     *
     * @param <T> тип переданного элемента - любой наследниик {@code WebElement}
     * @param element элемент, по которму выполняется клик
     */
    public static <T extends WebElement> void doubleClick(T element) {
        new Actions((WebDriver) Environment.getDriverService().getDriver()).doubleClick(getWebElement(element)).perform();
    }

    /**
     * Получает {@code WebElement}. Если элемент уже имеет тип
     * {@code WebElement}, то вернет его. Если наследник
     * {@code TypifiedElement}, то {@code WrappedElement}
     *
     * @param <T> тип переданного элемента - любой наследниик {@code WebElement}
     * @param element элемент, по которому нужно получить элемент типа
     * {@code WebElement}
     * @return преобразованный к {@code WebElement} элемент
     */
    public static <T extends WebElement> WebElement getWebElement(T element) {
        WebElement webElement;
        if (element instanceof TypifiedElement) {
            webElement = ((TypifiedElement) element).getWrappedElement();
        } else {
            webElement = element;
        }
        return webElement;
    }

    /**
     * Проверяет, что список элементов содержит текстовое значение, строго
     * равное переданному
     *
     * @param <T> тип переданного элемента - любой наследниик {@code WebElement}
     * @param elements список любых наследников {@code WebElement}
     * @param text искомый текст
     * @return Вернет {@code true}, если текст найден в списке элементов и
     * {@code false}, если не найден
     */
    public static <T extends WebElement> boolean isContains(List<T> elements, String text) {
        return elements.stream()
                .map(T::getText).anyMatch(text::equals);
    }

    /**
     * Проверяет, что атрибут элемента содержит переданное значение
     *
     * @param <T> тип переданного элемента - любой наследниик {@code WebElement}
     * @param element элемент - любой наследниик {@code WebElement}
     * @param attribute имя атрибута элемента в структуре DOM
     * @param partialAttributeValue часть значения атрибута, которую нужно
     * проверить
     * @return Вернет {@code true}, если значение найдено в атрибуте элемента и
     * {@code false}, если не найдено
     */
    public static <T extends WebElement> boolean isElementAttributeContains(T element, String attribute, String partialAttributeValue) {
        return getWebElement(element).getAttribute(attribute).contains(partialAttributeValue);
    }

    /**
     * Проверяет, что атрибут элемента равен переданному значению
     *
     * @param <T> тип переданного элемента - любой наследниик {@code WebElement}
     * @param element элемент - любой наследниик {@code WebElement}
     * @param attribute имя атрибута элемента в структуре DOM
     * @param attributeValue значение атрибута, которое нужно проверить
     * @return Вернет {@code true}, если переданное значение равно значению
     * атрибута элемента и {@code false}, если нет
     */
    public static <T extends WebElement> boolean isElementAttributeEquals(T element, String attribute, String attributeValue) {
        return getWebElement(element).getAttribute(attribute).equals(attributeValue);
    }

    /**
     * Возвращает элемент, атрибут которого равен переданному значению
     *
     * @param <T> тип переданного элемента - любой наследниик {@code WebElement}
     * @param elements список элементов
     * @param attribute имя атрибута элемента в структуре DOM
     * @param attributeValue значение атрибута
     * @return Вернет элемент, у которого атрибут равен переданному значению.
     * Если такой элемент в списке не найден, вернет {@code null}
     */
    public static <T extends WebElement> T getElementWithAttributeWithEmptyResult(List<T> elements, String attribute, String attributeValue) {
        return getElementByPredicateWithEmptyResult(elements, element -> element.getAttribute(attribute).equals(attributeValue));
    }

    /**
     * Возвращает элемент, атрибут которого равен переданному значению
     *
     * @param <T> тип переданного элемента - любой наследниик {@code WebElement}
     * @param elements список элементов
     * @param attribute имя атрибута элемента в структуре DOM
     * @param attributeValue значение атрибута
     * @return Вернет элемент, у которого атрибут равен переданному значению.
     * Если такой элемент в списке не найден, будет выброшено исключение
     */
    public static <T extends WebElement> T getElementWithAttribute(List<T> elements, String attribute, String attributeValue) {
        T element = getElementWithAttributeWithEmptyResult(elements, attribute, attributeValue);
        if (element == null) {
            throw new AutotestError(format("Элемент с атрибутом "
                    + "\"%s\" не найден в списке элементов", attributeValue));
        }
        return element;
    }

    /**
     * Возвращает элемент, атрибут которого содержит переданное значение
     *
     * @param <T> тип переданного элемента - любой наследниик {@code WebElement}
     * @param elements список элементов
     * @param attribute имя атрибута элемента в структуре DOM
     * @param partialAttributeValue значение атрибута
     * @return Вернет элемент, у которого атрибут содержит переданное значение.
     * Если такой элемент в списке не найден, вернет {@code null}
     */
    public static <T extends WebElement> T getElementWithPartAttributeWithEmptyResult(List<T> elements, String attribute, String partialAttributeValue) {
        return getElementByPredicateWithEmptyResult(elements, element
                -> element.getAttribute(attribute).contains(partialAttributeValue));
    }

    /**
     * Возвращает элемент, атрибут которого содержит переданное значение
     *
     * @param <T> тип переданного элемента - любой наследниик {@code WebElement}
     * @param elements список элементов
     * @param attribute имя атрибута элемента в структуре DOM
     * @param partialAttributeValue значение атрибута
     * @return Вернет элемент, у которого атрибут содержит переданное значение.
     * Если такой элемент в списке не найден, будет выброшено исключение
     */
    public static <T extends WebElement> T getElementWithPartAttribute(List<T> elements, String attribute, String partialAttributeValue) {
        T element = getElementWithPartAttributeWithEmptyResult(elements, attribute, partialAttributeValue);
        if (element == null) {
            throw new AutotestError(format("Элемент с атрибутом, "
                    + "содержащим текст \"%s\" не найден в списке элементов", partialAttributeValue));
        }
        return element;
    }

    public static <T extends WebElement> List<String> getElementsText(List<T> elements) {
        return elements.stream()
                .map(T::getText)
                .collect(Collectors.toList());
    }

    /**
     * Получает текстовое значение для элементов, которые могут содержать внутри
     * себя несколько элементов с текстом
     * <p>
     * Если внутри элемента нет других элементов с тектсом, то будет возвращен
     * непосредственно текст элемента, если он есть, если его нет, то пустая
     * строка
     * <p>
     * Если внутри несколько элементов с текстом, то их значения будут
     * объеденены в строку через пробел
     *
     * @param <T> Тип получаемого элемента
     * @param element Элемент, по которому нужно получить текст
     * @return Возвращает текстовое значение элемента
     */
    public static <T extends WebElement> String getTextOfComplexElement(T element) {
        return getTextOfComplexElement(element, " ");
    }

    /**
     * Получает текстовое значение для элементов, которые могут содержать внутри
     * себя несколько элементов с текстом
     * <p>
     * Если внутри элемента нет других элементов с тектсом, то будет возвращен
     * непосредственно текст элемента, если он есть, если его нет, то пустая
     * строка
     * <p>
     * Если внутри несколько элементов с текстом, то их значения будут
     * объеденены в строку через заданный разделитель
     *
     * @param <T> Тип получаемого элемента
     * @param element Элемент, по которому нужно получить текст
     * @param split разедлитель
     * @return Возвращает текстовое значение элемента
     */
    public static <T extends WebElement> String getTextOfComplexElement(T element, String split) {
        WebElement webElement = getWebElement(element);
        List<WebElement> textElements = webElement.findElements(By.xpath(TEXT_XPATH));

        StringBuilder value = new StringBuilder();
        String elementText = webElement.getText();

        if (textElements.isEmpty()) {
            value.append(elementText);
        } else {
            for (WebElement textElement : textElements) {
                value.append(textElement.getText()).append(split);
            }
        }
        // TODO Временное решение для элеметов, содержащих внутри кнопки с текстом 
        // тэгированный подтекст. Для них не работает стандартный getText() и 
        // не работает логика получения комплексного элемента
        if (elementText.length() - value.toString().length() >= 0) {
            value = new StringBuilder(elementText);
        }
        return value.toString().replace("\n", "").trim();
    }

    /**
     * Нахоодит элемент в списке элементов с заданным текстом и кликает по нему.
     * Проверяет, не является ли элемент недоступным для редактирования. Если
     * недоступен, кидает исключение
     *
     * @param <T> тип элементов
     * @param elements список элементов
     * @param text текстовое значение элемента, по которому нужно выполнить клик
     */
    public static <T extends WebElement> void clickOnElemenByText(List<T> elements, String text) {
        T element = getElementByText(elements, text);
        if (!element.isEnabled()) {
            throw new AutotestError(format("Элемент с тектсом \"%s\" не доступен для выбора.", text));
        }
        element.click();
    }

    /**
     * Находит элемент по его текстовому значению
     *
     * @param <T> тип элементов
     * @param elements список элементов
     * @param text текстовое значение элемента
     * @return Возвращает элемент заданного типа и с заданным текстовым
     * значением. Если такой элемент не найден, вернет {@code null}
     */
    public static <T extends WebElement> T getElementByTextWithEmptyResult(List<T> elements, String text) {
        return getElementByPredicateWithEmptyResult(elements, element -> text.equals(element.getText()));
    }

    /**
     * Находит элемент по его текстовому значению
     *
     * @param <T> тип элементов
     * @param elements список элементов
     * @param text текстовое значение элемента
     * @return Возвращает элемент заданного типа и с заданным текстовым
     * значением
     */
    public static <T extends WebElement> T getElementByText(List<T> elements, String text) {
        T element = getElementByTextWithEmptyResult(elements, text);
        if (element == null) {
            throw new AutotestError(format("Элемент с текстом \"%s\" не найден списке элементов.", text));
        }
        return element;
    }

    /**
     * Возвращает элемент списка по индексу
     *
     * @param <T> тип элементов
     * @param elements список элементов
     * @param index индекс элемента
     * @return Возвращает элемент заданного типа и с заданным индексом
     */
    public static <T extends WebElement> T getElementByIndex(List<T> elements, int index) {
        if (elements.isEmpty()) {
            throw new AutotestError("Список элементов пуст.");
        }
        if (index < elements.size() && index > -1) {
            return elements.get(index);
        } else {
            throw new AutotestError("Элемент с индексом " + index + " не найден.");
        }
    }

    /**
     * Получает текстовое значение элемента по его xpath выражению
     *
     * @param <T> тип элемента, относительно которого будем выполнять поиск
     * @param currentContext элемент, относительно которого будем выполнять
     * поиск
     * @param xpath путь до элемента
     * @return Возвращает текст элемента. Если элемент не был найден по
     * указанному пути, то возвращает пустую строку
     */
    public static <T extends WebElement> String getTextByElementXpath(T currentContext, String xpath) {
        List<WebElement> elements = getWebElement(currentContext).findElements(By.xpath(xpath));
        return elements.isEmpty() ? "" : elements.get(0).getText();
    }

    /**
     * Создает элемент с заданным кастомным типом из {@code WebElement} и
     * инициализирует его на текущей странице
     *
     * @param <T> желаемый тип элемента
     * @param clazz класс, экземпляр которого нужно создать
     * @param element элемент, из которого будем создавать кастомный
     * @return Возвращает элемент заданного типа
     */
    public static <T extends TypifiedElement> T createElementWithCustomType(Class<T> clazz, WebElement element) {
        T customElement = CustomHtmlElementLoader.createTypifiedElement(clazz, element, "Custom");
        CustomHtmlElementDecorator decorator = new CustomHtmlElementDecorator(new HtmlElementLocatorFactory(Environment.getDriverService().getDriver()));
        PageFactory.initElements(decorator, PageContext.getCurrentPage());
        return customElement;
    }

    /**
     * Возвращает элемент списка, соответствующий предикату
     *
     * @param <T> тип переданного элемента - любой наследниик {@code WebElement}
     * @param elements список элементов
     * @param predicate предикат, которому должен соответствовать искомый
     * элемент
     * @return Возвращает элемент списка, соответствующий предикату. Если такой
     * элемент не найден в списке, вернет {@code null}
     */
    public static <T extends WebElement> T getElementByPredicateWithEmptyResult(List<T> elements, Predicate<T> predicate) {
        return elements.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * Возвращает элемент списка, соответствующий предикату
     *
     * @param <T> тип переданного элемента - любой наследниик {@code WebElement}
     * @param elements список элементов
     * @param predicate предикат, которому должен соответствовать искомый
     * элемент
     * @return Возвращает элемент списка, соответствующий предикату. Если такой
     * элемент не найден в списке, будет выброшено исключение
     */
    public static <T extends WebElement> T getElementByPredicate(List<T> elements, Predicate<T> predicate) {
        return elements.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> new AutotestError("В списке элементов не найден элемент соответствующий предикату."));
    }
}
