package ru.sbtqa.tag.pagefactory.transformer;

import cucumber.api.Transformer;
import ru.sbtqa.tag.pagefactory.transformer.enums.Condition;

public class ConditionTransformer extends Transformer<Condition> {

    @Override
    public Condition transform(String value) {
        return Condition.fromString(value);
    }
}
