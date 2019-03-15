package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.ru.И;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.html.junit.HtmlSetupSteps;
import ru.sbtqa.tag.pagefactory.html.junit.HtmlSteps;

public class HtmlStepDefs {

    private final HtmlSteps htmlSteps = HtmlSteps.getInstance();

    @Before(order = 2)
    public void initHtml() {
        HtmlSetupSteps.initHtml();
    }
    
    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\)$")
    public void actionInBlock(String block, String action) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action);
    }

    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметрами из таблицы$")
    public void actionInBlock(String block, String action, DataTable dataTable) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action, dataTable);
    }

    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметром \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action, param);
    }

    @И("^(?:пользователь |он )?в блоке \"([^\"]*)\" \\(([^)]*)\\) с параметрами \"([^\"]*)\" \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param1, String param2) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action, param1, param2);
    }

    @И("^(?:пользователь |он )?в списке \"([^\"]*)\" находит элемент со значением \"([^\"]*)\"$")
    public void find(String listTitle, String value) throws PageException {
        htmlSteps.find(listTitle, value);
    }
}
