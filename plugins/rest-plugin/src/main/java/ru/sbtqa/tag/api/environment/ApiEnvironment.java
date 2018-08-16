package ru.sbtqa.tag.api.environment;

import ru.sbtqa.tag.api.repository.Repository;
import ru.sbtqa.tag.api.storage.BlankStorage;
import ru.sbtqa.tag.pagefactory.environment.Environment;

public class ApiEnvironment extends Environment {

    private static Repository repository;
    private static BlankStorage blankStorage;

    public static void setRepository(Repository repository) {
        ApiEnvironment.repository = repository;
    }

    public static Repository getRepository() {
        return repository;
    }

    public static BlankStorage getBlankStorage() {
        return blankStorage;
    }

    public static void setBlankStorage(BlankStorage blankStorage) {
        ApiEnvironment.blankStorage = blankStorage;
    }
}
