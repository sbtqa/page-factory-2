#language: en
@fragments
Feature: actions fragments

  @fragment
  Scenario: level2
    * user (fill the field) "first name" "LEVEL2"
    * user (click the button) "send"
    * user (check that error message not contains) "Please specify your first name"
