package ru.sbtqa.tag.pagefactory.transformer;

import cucumber.api.Transformer;
import ru.sbtqa.tag.pagefactory.transformer.enums.Presence;

public class PresenceTransformer extends Transformer<Presence> {

    @Override
    public Presence transform(String value) {
        return Presence.fromString(value);
    }
}
