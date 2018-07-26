#language: en
@fragments
Feature: actions fragments

  @fragment
  Scenario: level1-first
    * user (fill the field) "first name" "LEVEL1-FIRST"
    * user (click the button) "send"
    * user (check that error message not contains) "Please specify your first name"
    * user inserts fragment "level2"

  @fragment
  Scenario: level1-second
    * user (fill the field) "first name" "LEVEL2-FIRST"
    * user (click the button) "send"
    * user (check that error message not contains) "Please specify your first name"
    * user inserts fragment "level2"