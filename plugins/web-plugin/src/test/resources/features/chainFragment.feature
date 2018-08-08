#language: en
Feature: Check fragments functional

  @chain
  Scenario: chain
    * user is on the page "Main"
    * user (click the button) "Contact"
    * user is on the page "Contact"

    * user inserts fragment "level1-first"
    * user inserts fragment "level1-second"