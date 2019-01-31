package ru.sbtqa.tag.pagefactory.optional;

import gherkin.pickles.Argument;
import gherkin.pickles.PickleLocation;
import gherkin.pickles.PickleStep;
import java.util.List;

public class PickleStepCustom extends PickleStep {

    private Boolean isCritical;

    private Boolean isFailed = false;

    public PickleStepCustom(String text, List<Argument> arguments, List<PickleLocation> locations, Boolean isCritical) {
        super(text, arguments, locations);
        this.isCritical = isCritical;
    }

    public void setIsFailed(Boolean isFailed){
        this.isFailed = isFailed;
    }

    public boolean isCritical() {
        return isCritical;
    }

    public boolean isFailed() {
        return isFailed;
    }
}
