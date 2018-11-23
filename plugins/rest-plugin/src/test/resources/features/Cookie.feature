#language:en
@cookie
Feature: Send get with cookie

  @cookie
  Scenario: Send get with cookie
    * user sends request for "cookie"
    * system returns "result"