package ru.sbtqa.tag.api.repository;

import ru.sbtqa.tag.api.ApiEntry;

public interface Repository {

    void add(Class<? extends ApiEntry> apiEntry, ApiPair pair);
    ApiPair get(Class<? extends ApiEntry> apiEntry);
    ApiPair getLast();
}
