package ru.sbtqa.tag.pagefactory.html.stepdefs;

import cucumber.api.java.Before;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.html.actions.HtmlPageActions;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

public class HtmlSetupStepDefs {
    
    @Before
    public void initHTML() {
        Environment.setDriverService(new WebDriverService());
        Environment.setPageActions(new HtmlPageActions());
    }
}
