package ru.sbtqa.tag.api.repository;

import ru.sbtqa.tag.api.Entry;

public interface Repository {

    void add(Class<? extends Entry> apiEntry, ApiPair pair);
    ApiPair get(Class<? extends Entry> apiEntry);
    ApiPair getLast();
}
