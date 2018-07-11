package ru.sbtqa.tag.pagefactory;

import cucumber.api.event.TestSourceRead;
import cucumber.runner.EventBus;
import cucumber.runtime.CucumberException;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.Resource;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.FeatureBuilder;
import cucumber.runtime.model.PathWithLines;
import cucumber.util.Encoding;
import gherkin.ast.GherkinDocument;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CucumberFeatureExt extends CucumberFeature {

    public CucumberFeatureExt(GherkinDocument gherkinDocument, String uri, String gherkinSource) {
        super(gherkinDocument, uri, gherkinSource);
    }
}
