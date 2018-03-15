package ru.sbtqa.tag.pagefactory.htmlstepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import org.openqa.selenium.NoSuchElementException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.htmlstepdefs.HTMLStepDefs;

public class StepDefs extends HTMLStepDefs {

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in block \"([^\"]*)\" \\((.*?)\\)$")
    public void userActionInBlockNoParams(String block, String action) throws PageInitializationException,
            NoSuchMethodException, NoSuchElementException {
        super.userActionInBlockNoParams(block, action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in block \"([^\"]*)\" \\((.*?)\\) with the parameters of table$")
    public void userActionInBlockTableParam(String block, String action, DataTable dataTable) throws PageInitializationException, NoSuchMethodException {
        super.userActionInBlockTableParam(block, action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in block \"([^\"]*)\" \\((.*?)\\) with a parameter \"([^\"]*)\"$")
    public void userActionInBlockOneParam(String block, String action, String param) throws PageInitializationException, NoSuchMethodException {
        super.userActionInBlockOneParam(block, action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in block \"([^\"]*)\" \\((.*?)\\) with the parameters \"([^\"]*)\"  \"([^\"]*)\"$")
    public void userActionInBlockTwoParams(String block, String action, String param1, String param2) throws PageInitializationException, NoSuchMethodException {
        super.userActionInBlockTwoParams(block, action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in block \"([^\"]*)\" finds (element|textinput|checkbox|radiobutton|table|header|button|link|image) \"([^\"]*)\"$")
    public void findElementInBlock(String block, String elementType, String elementTitle) throws PageException {
        super.findElementInBlock(block, elementType, elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user in list \"([^\"]*)\" finds the value element \"([^\"]*)\"$")
    public void findElementInList(String listTitle, String value) throws PageException {
        super.findElementInList(listTitle, value);
    }
}
