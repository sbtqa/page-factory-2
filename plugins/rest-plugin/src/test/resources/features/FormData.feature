#language:en
@form
Feature: Send post with form parameters

  @form
  Scenario: Send post with form parameters
    * user sends request for "form"
    * system returns "result"