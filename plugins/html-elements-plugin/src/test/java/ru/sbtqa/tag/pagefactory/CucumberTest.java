package ru.sbtqa.tag.pagefactory;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true,
        glue = {"ru.sbtqa.tag.stepdefs",
                "setting"
        },
        features = {"src/test/resources/features"})
public class CucumberTest {
}