package ru.sbtqa.tag.api.utils;

import ru.sbtqa.tag.api.dto.Client;

public class TestDataUtils {

    private TestDataUtils() {}

    public static Client createDefaultClient() {
        Client client = new Client();
        client.setId(Default.ID);
        client.setName(Default.NAME);
        client.setEmail(Default.EMAIL);

        return client;
    }
}
