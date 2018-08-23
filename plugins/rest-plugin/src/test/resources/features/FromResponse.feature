#language:en
@fromresponse
Feature: From response annotation test

  Scenario: From response annotation test
    * user sends request for "from response first"
    * user sends request for "from response second"
    * system returns "result"
