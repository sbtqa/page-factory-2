package ru.sbtqa.tag.api.storage;

import ru.sbtqa.tag.api.exception.RestPluginException;

import java.util.ArrayList;
import java.util.List;

/**
 * The repository for {@link EndpointBlank} storing
 */
public class BlankStorage {

    List<EndpointBlank> blanks = new ArrayList<>();

    public EndpointBlank add(EndpointBlank endpointBlank) {
        blanks.add(endpointBlank);
        return endpointBlank;
    }

    public EndpointBlank get(String title) {
        for (EndpointBlank blank : blanks) {
            if (blank.getTitle().equals(title)) {
                return blank;
            }
        }

        return add(new EndpointBlank(title));
    }

    public EndpointBlank getLast() {
        if (!blanks.isEmpty()) {
            return blanks.get(blanks.size() - 1);
        } else {
            throw new RestPluginException("The blank repository is empty");
        }
    }
}
