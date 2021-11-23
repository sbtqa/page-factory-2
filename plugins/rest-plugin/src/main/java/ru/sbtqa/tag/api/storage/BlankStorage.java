package ru.sbtqa.tag.api.storage;

import java.util.ArrayList;
import java.util.List;
import ru.sbtqa.tag.api.exception.RestPluginException;

/**
 * The repository for {@link EndpointBlank} storing
 */
public class BlankStorage {

    final List<EndpointBlank> blanks = new ArrayList<>();

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

            System.out.println("!!!! = " + blanks.get(blanks.size() - 1));

            return blanks.get(blanks.size() - 1);
        } else {
            throw new RestPluginException("The blank repository is empty");
        }
    }

    public void removeLast() {
        if (!blanks.isEmpty()) {
            blanks.remove(blanks.size() - 1);
        } else {
            throw new RestPluginException("The blank repository is empty");
        }
    }
}
