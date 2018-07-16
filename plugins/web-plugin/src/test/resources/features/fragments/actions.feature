#language: en
@fragments
Feature: actions fragments

  @fragment=click_fragment
  Scenario: click fragment
    * user (click the button) "send"
    * user (check that error message contains) "Please specify your first name"

  @fragment=fill_fragment
  Scenario: fill fragment
    * user (fill the field) "first name" "<first name>"
    * user (click the button) "<button name>"
    * user (check that error message not contains) "Please specify your first name"