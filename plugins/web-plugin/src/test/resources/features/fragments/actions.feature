#language: en
@fragments
Feature: actions fragments

  Scenario: click fragment
    * user (click the button) "send"
    * user (check that error message contains) "Please specify your first name"

  Scenario: fill fragment
    * user (fill the field) "first name" "Alex"
    * user (click the button) "send"
    * user (check that error message not contains) "Please specify your first name"