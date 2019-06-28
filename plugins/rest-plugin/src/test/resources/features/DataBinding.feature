#language:en
@fixtures
Feature: Send get with cookie

  @data=$Positive
  Scenario: Insert triple data
    * print text "${Vehicle.brand}" , "${Vehicle.year}" , "${Vehicle.model}"
    * user sends request for "cookie"
    * system returns "result"
