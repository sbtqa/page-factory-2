package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.ru.И;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.stepdefs.HtmlSetupSteps;
import ru.sbtqa.tag.stepdefs.HtmlSteps;

public class HtmlStepDefs extends HtmlSteps {

    @Before(order = 2)
    public void initHtml() {
        HtmlSetupSteps.initHtml();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\)$")
    public void actionInBlock(String block, String action) throws NoSuchMethodException {
        super.actionInBlock(block, action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметрами из таблицы$")
    public void actionInBlock(String block, String action, DataTable dataTable) throws NoSuchMethodException {
        super.actionInBlock(block, action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметром \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param) throws NoSuchMethodException {
        super.actionInBlock(block, action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметрами \"([^\"]*)\" \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param1, String param2) throws NoSuchMethodException {
        super.actionInBlock(block, action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" находит (элемент|текстовое поле|чекбокс|радиокнопка|таблицу|заголовок|кнопку|ссылку|изображение) \"([^\"]*)\"$")
    public void find(String block, String elementType, String elementTitle) throws PageException {
        super.find(block, elementType, elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в списке \"([^\"]*)\" находит элемент со значением \"([^\"]*)\"$")
    public void find(String listTitle, String value) throws PageException {
        super.find(listTitle, value);
    }
}
