package ru.sbtqa.tag.api.environment;

import ru.sbtqa.tag.api.repository.Repository;
import ru.sbtqa.tag.api.storage.BlankStorage;
import ru.sbtqa.tag.pagefactory.environment.Environment;

public class ApiEnvironment extends Environment {

    private static final ThreadLocal<Repository> repository = new ThreadLocal<>();
    private static final ThreadLocal<BlankStorage> blankStorage = new ThreadLocal<>();

    public static void setRepository(Repository repository) {
        ApiEnvironment.repository.set(repository);
    }

    public static Repository getRepository() {
        return repository.get();
    }

    public static BlankStorage getBlankStorage() {
        return blankStorage.get();
    }

    public static void setBlankStorage(BlankStorage blankStorage) {
        ApiEnvironment.blankStorage.set(blankStorage);
    }
}
