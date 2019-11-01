package ru.sbtqa.tag.pagefactory.html.junit;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.elements.radiogroup.RadioGroupAbstract;
import ru.sbtqa.tag.pagefactory.transformer.ContainCondition;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;

import static java.lang.String.format;
import static java.lang.ThreadLocal.withInitial;

public class ControlSteps implements Steps {

    private static final String IS_NOT_CONTROL_ERROR_TEMPLATE = "Cannot mark element. Element is not a checkbox: %s";

    static final ThreadLocal<ControlSteps> storage = withInitial(ControlSteps::new);

    public static ControlSteps getInstance() {
        return storage.get();
    }

    /**
     * Marks a control or radio button
     *
     * @param flagName control name
     * @return Returns itself
     */
    public ControlSteps select(String flagName) {
        WebElement element = getFindUtils().find(flagName);
        if (element instanceof CheckBox) {
            ((CheckBox) element).select();
        } else if (element instanceof Button) {
            element.click();
        } else {
            throw new AutotestError(format(IS_NOT_CONTROL_ERROR_TEMPLATE, flagName));
        }
        return this;
    }

    /**
     * Checks whether a control is marked
     *
     * @param flagName control name
     * @param condition if {@code Condition.POSITIVE} checks that a control is selected,
     * if (@code Condition.NEGATIVE) checks that a control is not selected
     * @return Returns itself
     */
    public ControlSteps isSelected(String flagName, ContainCondition condition) {
        WebElement element = getFindUtils().find(flagName);
        boolean isSelected = element.isSelected();
        Assert.assertEquals("The element " + (isSelected ? "not " : "") + "checked: " + flagName, condition.isPositive(), element.isSelected());
        return this;
    }

    /**
     * Marks a control by value
     *
     * @param flagName control name
     * @param value value to set. For checkboxes - {@code true} or {@code false}
     * @return Returns itself
     */
    public ControlSteps selectByValue(String flagName, String value) {
        WebElement element = getFindUtils().find(flagName);
        if (element instanceof RadioGroupAbstract) {
            ((RadioGroupAbstract) element).selectByValue(value);
        } else if (element instanceof CheckBox) {
            ((CheckBox) element).set(Boolean.valueOf(value));
        } else {
            throw new AutotestError(format(IS_NOT_CONTROL_ERROR_TEMPLATE, flagName));
        }
        return this;
    }

    /**
     * Checks value for selectivity
     *
     * @param groupName control name
     * @param condition if {@code Condition.POSITIVE} checks that the value is selected,
     * if (@code Condition.NEGATIVE) checks that the value is not selected
     * @param value radio group value to be checked
     * @return Returns itself
     */
    public ControlSteps isRadiobuttonSelected(String groupName, ContainCondition condition, String value) {
        RadioGroupAbstract element = getFindUtils().find(groupName, RadioGroupAbstract.class);
        boolean check = element.getSelectedValue().equals(value);
        Assert.assertEquals("The element of radio group \"" + groupName + "\" "
                + (check ? "not " : "") + " checked: " + value, condition.isPositive(), check);
        return this;
    }

    /**
     * Check radio group value not selected
     *
     * @param radioGroupName radio group name
     * @return Returns itself
     */
    public ControlSteps checkRadioGroupValueNotSelected(String radioGroupName) {
        RadioGroupAbstract radioGroup = getFindUtils().find(radioGroupName, RadioGroupAbstract.class);
        Assert.assertFalse("The radio group has selected value", radioGroup.hasSelectedValue());
        return this;
    }
}
