#language: en
@fragments
Feature: fragments with non critical

  @fragment
  Scenario: fill fragment with non critical
    * ? user fills the field "first name" "<first name>"
    * ? user clicks the button "<button name>"
    * ? user (check that error message contains) "Please specify your first name"
    * ? user (check that error message not contains) "Please specify your first name"