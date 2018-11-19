package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.ru.И;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.stepdefs.HtmlSetupSteps;
import ru.sbtqa.tag.stepdefs.HtmlGenericSteps;

public class HtmlStepDefs extends HtmlGenericSteps<HtmlStepDefs> {

    @Before(order = 2)
    public void initHtml() {
        HtmlSetupSteps.initHtml();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\)$")
    public HtmlStepDefs actionInBlock(String block, String action) throws NoSuchMethodException {
        return super.actionInBlock(block, action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметрами из таблицы$")
    public HtmlStepDefs actionInBlock(String block, String action, DataTable dataTable) throws NoSuchMethodException {
        return super.actionInBlock(block, action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметром \"([^\"]*)\"$")
    public HtmlStepDefs actionInBlock(String block, String action, String param) throws NoSuchMethodException {
        return super.actionInBlock(block, action, param);
    }

    /**
     * {@inheritDoc}
     */
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметрами \"([^\"]*)\" \"([^\"]*)\"$")
    public HtmlStepDefs actionInBlock(String block, String action, String param1, String param2) throws NoSuchMethodException {
        return super.actionInBlock(block, action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" находит (элемент|текстовое поле|чекбокс|радиокнопка|таблицу|заголовок|кнопку|ссылку|изображение) \"([^\"]*)\"$")
    public HtmlStepDefs find(String block, String elementType, String elementTitle) throws PageException {
        return super.find(block, elementType, elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?в списке \"([^\"]*)\" находит элемент со значением \"([^\"]*)\"$")
    public HtmlStepDefs find(String listTitle, String value) throws PageException {
        return super.find(listTitle, value);
    }
}
