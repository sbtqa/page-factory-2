package ru.sbtqa.tag.pagefactory.optional;

import cucumber.api.PickleStepTestStep;
import gherkin.pickles.PickleStep;
import java.util.List;

public class PickleStepCustom implements PickleStepTestStep, CriticalTestStep {

    public static final String NON_CRITICAL = "? ";

    private Boolean isCritical = true;
    private Boolean isSkipped = false;
    private String data;
    private Throwable error = null;
    private String log = null;

    PickleStepTestStep pickleStepTestStep;

    public PickleStepCustom(PickleStepTestStep pickleStepTestStep) {
        this.pickleStepTestStep = pickleStepTestStep;
        this.isCritical = pickleStepTestStep.getPickleStep().getText().startsWith(NON_CRITICAL);
//        super(step.getText(), step.getArgument(), step.getLocations());
//        this.setCritical(!step.getText().startsWith(NON_CRITICAL));
    }

    public boolean isCritical() {
        return isCritical;
    }

//    public boolean isSkipped() {
//        return this.isSkipped;
//    }
//
//    public Throwable getError() {
//        return error;
//    }
//
//    public void setError(Throwable error) {
//        this.error = error;
//    }
//
//    public boolean hasError() {
//        return this.error != null;
//    }
//
//    public String getLog() {
//        return log;
//    }
//
//    public void setLog(String log) {
//        this.log = log;
//    }
//
//    public boolean hasLog() {
//        return this.log != null;
//    }
//
//    public void setText(String text) {
//        try {
//            FieldUtils.writeField(this, "text", text, true);
//        } catch (IllegalAccessException ex) {
//            throw new ReadFieldError("Error reading the field: text");
//        }
//    }
//
//    public String getData() {
//        return data;
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }
//

//
//    public void replaceNonCriticalText() {
//        if (!this.isCritical) {
//            this.setText(this.getText().replaceFirst("\\" + NON_CRITICAL, ""));
//        }
//    }
//
//    public void setSkipped(Boolean skipped) {
//        isSkipped = skipped;
//    }

    @Override
    public PickleStep getPickleStep() {
        return pickleStepTestStep.getPickleStep();
    }

    @Override
    public String getStepLocation() {
        return pickleStepTestStep.getStepLocation();
    }

    @Override
    public int getStepLine() {
        return pickleStepTestStep.getStepLine();
    }

    @Override
    public String getStepText() {
        return pickleStepTestStep.getStepText();
    }

    @Override
    public List<cucumber.api.Argument> getDefinitionArgument() {
        return pickleStepTestStep.getDefinitionArgument();
    }

    @Override
    public List<gherkin.pickles.Argument> getStepArgument() {
        return pickleStepTestStep.getStepArgument();
    }

    @Override
    public String getPattern() {
        return pickleStepTestStep.getPattern();
    }

    @Override
    public String getCodeLocation() {
        return pickleStepTestStep.getCodeLocation();
    }
}
