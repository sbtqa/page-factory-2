#language: en
@fragment-non-critical
Feature: non critical fragments

  @fragment
  Scenario: fill non critical fragment
    * user fills the field "first name" "<first name>"
    * user clicks the button "<button name>"
    * user (check that error message not contains) "Please specify your first name"