package ru.sbtqa.tag.pagefactory.reflection;

import static java.lang.String.format;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exception.ElementSearchException;
import ru.sbtqa.tag.pagefactory.exceptions.NoSuchActionException;
import ru.sbtqa.tag.pagefactory.find.HtmlFindUtils;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

public class HtmlReflection extends DefaultReflection {

    /**
     * Execute method with one or more parameters inside of the given block
     * element
     *
     * @param blockPath block title, or a block chain string separated with
     * {@code ->} symbols
     * @param actionTitle title of the action to execute
     * @param parameters parameters that will be passed to method
     */
    public void executeMethodByTitleInBlock(String blockPath, String actionTitle, Object... parameters) {
        try {
            HtmlElement block = ((HtmlFindUtils) Environment.getFindUtils()).find(blockPath, HtmlElement.class);
            executeMethodByTitle(block, actionTitle, parameters);
        } catch (NoSuchMethodException ex) {
            throw new NoSuchActionException(format("Action '%s' not found in block '%s'", actionTitle, blockPath), ex);
        } catch (ElementSearchException ex) {
            throw new ElementSearchException(format("Block not found by path '%s'", blockPath), ex);
        }
    }
}
