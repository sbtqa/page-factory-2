#language: en
@fragments
Feature: actions fragments

  @fragment
  Scenario: click fragment
    * user clicks the button "send"
    * user (check that error message contains) "Please specify your first name"

  @fragment
  Scenario: fill fragment
    * user fills the field "first name" "<first name>"
    * user clicks the button "<button name>"
    * user (check that error message not contains) "Please specify your first name"