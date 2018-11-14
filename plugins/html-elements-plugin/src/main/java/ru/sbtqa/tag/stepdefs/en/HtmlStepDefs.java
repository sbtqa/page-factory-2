package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
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
    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\)$")
    public void actionInBlock(String block, String action) throws NoSuchMethodException {
        super.actionInBlock(block, action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\) with the parameters of table$")
    public void actionInBlock(String block, String action, DataTable dataTable) throws NoSuchMethodException {
        super.actionInBlock(block, action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\) with a parameter \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param) throws NoSuchMethodException {
        super.actionInBlock(block, action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\) with the parameters \"([^\"]*)\" \"([^\"]*)\"$")
    public void actionInBlock(String block, String action, String param1, String param2) throws NoSuchMethodException {
        super.actionInBlock(block, action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in block \"([^\"]*)\" finds (element|textinput|checkbox|radiobutton|table|header|button|link|image) \"([^\"]*)\"$")
    public void find(String block, String elementType, String elementTitle) throws PageException {
        super.find(block, elementType, elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in list \"([^\"]*)\" finds the value element \"([^\"]*)\"$")
    public void find(String listTitle, String value) throws PageException {
        super.find(listTitle, value);
    }
}
