package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.api.ApiEnvironment;
import ru.sbtqa.tag.api.repository.ApiRepository;

public class ApiSetupSteps {

    public void initApi() {
        ApiEnvironment.setRepository(new ApiRepository());
    }
}
