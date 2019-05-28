package ru.sbtqa.tag.pagefactory.html.junit;

import org.junit.Assert;
import ru.sbtqa.tag.pagefactory.elements.hint.Hint;
import ru.sbtqa.tag.pagefactory.transformer.enums.Condition;
import ru.sbtqa.tag.pagefactory.transformer.enums.SearchStrategy;
import ru.sbtqa.tag.pagefactory.web.utils.ElementUtils;
import static java.lang.ThreadLocal.withInitial;

public class HintSteps implements Steps {

    static final ThreadLocal<HintSteps> storage = withInitial(HintSteps::new);

    public static HintSteps getInstance() {
        return storage.get();
    }

    public HintSteps open(String hintName) {
        getFindUtils().find(hintName, Hint.class).open();
        return this;
    }

    public HintSteps close(String hintName) {
        getFindUtils().find(hintName, Hint.class).close();
        return this;
    }

    public HintSteps hasText(String hintName, String text) {
        Hint hint = getFindUtils().find(hintName, Hint.class);
        ElementUtils.checkText(hint.getText(), text, SearchStrategy.EQUALS);
        return this;
    }

    public HintSteps containsText(String hintName, String text) {
        Hint hint = getFindUtils().find(hintName, Hint.class);
        ElementUtils.checkText(hint.getText(), text, SearchStrategy.CONTAINS);
        return this;
    }

    public HintSteps isOpen(Condition negation, String hintName) {
        boolean isPositive = negation.equals(Condition.POSITIVE);
        boolean isOpenActual = getFindUtils().find(hintName, Hint.class).isOpen();
        Assert.assertEquals("Hint is " + (isPositive ? "" : "not ") + "displayed", isPositive, isOpenActual);
        return this;
    }
}
