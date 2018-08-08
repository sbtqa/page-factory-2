#language: en
Feature: Check fragments functional

  @chain
  Scenario: chain
    * user is on the page "Main"
    * user (click the button) "Contact"
    * user is on the page "Contact"

    * user inserts fragment "level1-first"
      | first name | last name |
      | Bob        | Burnquist |
    * user (checks value) "first name" "Bob"
    * user (checks value) "last name" "Burnquist"
    * user inserts fragment "level1-second"

