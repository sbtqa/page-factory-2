#language:en
@form
Feature: Send post with form parameters

  @form
  Scenario: Send post with form parameters
    * user sends request for "form"
    * system returns "result"

  @form-in-row
  Scenario: Send post several in a row
    * user sends request for "form without preset" with parameters
      | id   | 999          |
      | name | feature-name |
    * system returns "result with data" with parameters
      | result | 999feature-namenull |
    * user sends request for "form without preset" with parameters
      | name  | second-feature-name |
      | email | feature-email       |
    * system returns "result with data" with parameters
      | result | 0second-feature-namefeature-email |
