package ru.sbtqa.tag.pagefactory.optional;

import gherkin.pickles.PickleStep;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.pagefactory.exceptions.ReadFieldError;

public class PickleStepCustom extends PickleStep implements CriticalTestStep {

    public static final String NON_CRITICAL = "? ";

    private boolean isNonCritical;
    private boolean isSkipped = false;
    private String dataTag;
    private Throwable error = null;
    private String log = null;

    public PickleStepCustom(PickleStep pickleStep) {
        super(pickleStep.getText(), pickleStep.getArgument(), pickleStep.getLocations());
        this.isNonCritical = pickleStep.getText().startsWith(NON_CRITICAL);
    }

    @Override
    public boolean isNonCritical() {
        return isNonCritical;
    }

    public boolean isSkipped() {
        return this.isSkipped;
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

    public String getDataTag() {
        return dataTag;
    }

    public void setDataTag(String dataTag) {
        this.dataTag = dataTag;
    }

    public void removeNonCriticalSign() {
        if (isNonCritical) {
            this.setText(this.getText().replaceFirst("\\" + NON_CRITICAL, ""));
        }
    }

    public void setSkipped(Boolean skipped) {
        isSkipped = skipped;
    }
}
