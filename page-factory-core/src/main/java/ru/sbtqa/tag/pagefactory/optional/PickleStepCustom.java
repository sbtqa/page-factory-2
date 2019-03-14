package ru.sbtqa.tag.pagefactory.optional;

import gherkin.pickles.PickleStep;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.pagefactory.exceptions.ReadFieldError;

public class PickleStepCustom extends PickleStep {

    public static final String NON_CRITICAL = "? ";

    private Boolean isCritical = true;
    private Boolean isSkipped = false;
    private Throwable error = null;
    private String log = null;

    public final PickleStep step;

    public PickleStepCustom(PickleStep step) {
        super(step.getText(), step.getArgument(), step.getLocations());
        this.step = step;
        this.isCritical = !step.getText().startsWith(NON_CRITICAL);
        if (!this.isCritical) {
            this.setText(step.getText().replaceFirst("\\" + NON_CRITICAL, ""));
        }
    }

    public PickleStepCustom(PickleStep step, Boolean isSkipped) {
        super(step.getText(), step.getArgument(), step.getLocations());
        this.step = step;
        this.isSkipped = isSkipped;
    }

    public boolean isCritical() {
        return isCritical;
    }

    public boolean isSkipped() {
        return isSkipped;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public boolean hasError() {
        return this.error != null;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public boolean hasLog() {
        return this.log != null;
    }

    public void setText(String text) {
        try {
            FieldUtils.writeField(this, "text", text, true);
        } catch (IllegalAccessException ex) {
            throw new ReadFieldError("Error reading the field: text");
        }
    }
}
