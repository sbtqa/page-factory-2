package ru.sbtqa.tag.pagefactory.pages.htmlelements.blocks;

import java.util.List;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.Link;

public class MenuBlockListVersion extends HtmlElement {

    @ElementTitle("toolbar")
    @FindBy(xpath = ".//a")
    private List<Link> toolbar;
}
