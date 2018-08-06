package ru.sbtqa.tag.api;

public interface Repository {

    void add(Class<? extends ApiEntry> apiEntry, ApiPair pair);
    ApiPair get(Class<? extends ApiEntry> apiEntry);
    ApiPair getLast();
}
