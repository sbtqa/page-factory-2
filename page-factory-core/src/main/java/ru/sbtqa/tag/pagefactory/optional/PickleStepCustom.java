package ru.sbtqa.tag.pagefactory.optional;

import gherkin.pickles.Argument;
import gherkin.pickles.PickleLocation;
import gherkin.pickles.PickleStep;
import java.util.List;

public class PickleStepCustom extends PickleStep {

    private Boolean isCritical;
    public final PickleStep step;

    public PickleStepCustom(PickleStep step,String text, Boolean isCritical) {
        super(text, step.getArgument(), step.getLocations());
        this.step = step;
        this.isCritical = isCritical;
    }

    public boolean isCritical() {
        return isCritical;
    }
}
