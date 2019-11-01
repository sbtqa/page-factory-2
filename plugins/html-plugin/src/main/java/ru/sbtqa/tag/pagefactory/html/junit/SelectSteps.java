package ru.sbtqa.tag.pagefactory.html.junit;

import org.junit.Assert;
import ru.sbtqa.tag.pagefactory.elements.select.SelectAbstract;
import ru.sbtqa.tag.pagefactory.transformer.SearchStrategy;
import static java.lang.ThreadLocal.withInitial;

public class SelectSteps implements Steps {

    static final ThreadLocal<SelectSteps> storage = withInitial(SelectSteps::new);

    public static SelectSteps getInstance() {
        return storage.get();
    }

    public SelectSteps select(String selectName, String selectValue) {
        getSelect(selectName).selectByValue(selectValue);
        return this;
    }

    public SelectSteps selectByTextFragment(String selectName, String textFragment) {
        getSelect(selectName).selectByValue(textFragment, SearchStrategy.CONTAINS);
        return this;
    }

    public SelectSteps selectByTextOfElement(String selectName, String selectValue, String selectField) {
        getSelect(selectName).selectByValue(selectValue, selectField);
        return this;
    }

    public SelectSteps checkSelectedOptionByElement(String selectName, String selectValue, String selectField) {
        SelectAbstract selectComplex = getSelect(selectName);
        Assert.assertEquals(selectComplex.getSelectedOption().getElement(selectField).getText(), selectValue);
        return this;
    }

    public SelectSteps checkSelectedOption(String selectName, String selectValue) {
        SelectAbstract select = getSelect(selectName);
        Assert.assertEquals(select.getSelectedOption().getText(), selectValue);
        return this;
    }

    public SelectSteps open(String selectName) {
        getSelect(selectName).open();
        return this;
    }

    public SelectSteps close(String selectName) {
        getSelect(selectName).close();
        return this;
    }

    private SelectAbstract getSelect(String selectName) {
        return getFindUtils().find(selectName, SelectAbstract.class);
    }
}
