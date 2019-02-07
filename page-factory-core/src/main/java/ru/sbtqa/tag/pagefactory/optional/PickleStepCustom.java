package ru.sbtqa.tag.pagefactory.optional;

import gherkin.pickles.Argument;
import gherkin.pickles.PickleLocation;
import gherkin.pickles.PickleStep;
import java.util.List;

public class PickleStepCustom extends PickleStep {

    private Boolean isCritical;

    public PickleStepCustom(String text, List<Argument> arguments, List<PickleLocation> locations, Boolean isCritical) {
        super(text, arguments, locations);
        this.isCritical = isCritical;
    }

    public boolean isCritical() {
        return isCritical;
    }
}
