package ru.sbtqa.tag.api.repository;

import ru.sbtqa.tag.api.EndpointEntry;

public interface Repository {

    void add(Class<? extends EndpointEntry> endpoint, ApiPair pair);
    ApiPair get(Class<? extends EndpointEntry> endpoint);
    ApiPair getLast();
}
