package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.ru.И;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.stepdefs.HtmlGenericStepDefs;

public class HtmlStepDefs extends HtmlGenericStepDefs {

    @Override
    @Before(order = 2)
    public void initHtml() {
        super.initHtml();
    }

    @Override
    @After(order = 9999)
    public void disposeHtml() {
        super.disposeHtml();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\)$")
    public void userActionInBlockNoParams(String block, String action) throws NoSuchMethodException {
        super.userActionInBlockNoParams(block, action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметрами из таблицы$")
    public void userActionInBlockTableParam(String block, String action, DataTable dataTable) throws NoSuchMethodException {
        super.userActionInBlockTableParam(block, action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметром \"([^\"]*)\"$")
    public void userActionInBlockOneParam(String block, String action, String param) throws NoSuchMethodException {
        super.userActionInBlockOneParam(block, action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметрами \"([^\"]*)\" \"([^\"]*)\"$")
    public void userActionInBlockTwoParams(String block, String action, String param1, String param2) throws NoSuchMethodException {
        super.userActionInBlockTwoParams(block, action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" находит (элемент|текстовое поле|чекбокс|радиокнопка|таблицу|заголовок|кнопку|ссылку|изображение) \"([^\"]*)\"$")
    public void findElementInBlock(String block, String elementType, String elementTitle) throws PageException {
        super.findElementInBlock(block, elementType, elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в списке \"([^\"]*)\" находит элемент со значением \"([^\"]*)\"$")
    public void findElementInList(String listTitle, String value) throws PageException {
        super.findElementInList(listTitle, value);
    }
}
