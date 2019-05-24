package ru.sbtqa.tag.pagefactory.html.junit;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.find.HtmlFindUtils;

public interface Steps {

    default <T extends Steps> T context(T context) {
        return context;
    }

    default HtmlFindUtils getFindUtils(){
        return Environment.getFindUtils();
    }
}
