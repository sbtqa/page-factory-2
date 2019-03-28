package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.html.junit.HtmlSetupSteps;
import ru.sbtqa.tag.pagefactory.html.junit.HtmlSteps;

public class HtmlStepDefs {

    private final HtmlSteps htmlSteps = HtmlSteps.getInstance();

    @Before(order = 2)
    public void initHtml() {
        HtmlSetupSteps.initHtml();
    }

    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\)$")
    public void actionInBlock(String block, String action) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action);
    }

    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\) with the parameters of table$")
    public void actionInBlock(String block, String action, DataTable dataTable) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action, dataTable);
    }

    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\) with a parameter \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action, param);
    }

    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\) with the parameters \"([^\"]*)\" \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param1, String param2) throws NoSuchMethodException {
        htmlSteps.actionInBlock(block, action, param1, param2);
    }

    @And("^user in list \"([^\"]*)\" finds the value element \"([^\"]*)\"$")
    public void find(String listTitle, String value) throws PageException {
        htmlSteps.find(listTitle, value);
    }
}
