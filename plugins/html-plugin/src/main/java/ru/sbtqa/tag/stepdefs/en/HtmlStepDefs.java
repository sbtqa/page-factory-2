package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.stepdefs.HtmlGenericSteps;
import ru.sbtqa.tag.stepdefs.HtmlSetupSteps;

public class HtmlStepDefs extends HtmlGenericSteps<HtmlStepDefs> {

    @Before(order = 2)
    public void initHtml() {
        HtmlSetupSteps.initHtml();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\)$")
    public HtmlStepDefs actionInBlock(String block, String action) throws NoSuchMethodException {
        return super.actionInBlock(block, action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\) with the parameters of table$")
    public HtmlStepDefs actionInBlock(String block, String action, DataTable dataTable) throws NoSuchMethodException {
        return super.actionInBlock(block, action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\) with a parameter \"([^\"]*)\"$")
    public HtmlStepDefs actionInBlock(String block, String action, String param) throws NoSuchMethodException {
        return super.actionInBlock(block, action, param);
    }

    /**
     * {@inheritDoc}
     */
    @And("^user in block \"([^\"]*)\" \\(([^)]*)\\) with the parameters \"([^\"]*)\" \"([^\"]*)\"$")
    public HtmlStepDefs actionInBlock(String block, String action, String param1, String param2) throws NoSuchMethodException {
        return super.actionInBlock(block, action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in list \"([^\"]*)\" finds the value element \"([^\"]*)\"$")
    public HtmlStepDefs find(String listTitle, String value) throws PageException {
        return super.find(listTitle, value);
    }
}
