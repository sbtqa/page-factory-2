#language: en
Feature: Check fragments functional

  @chain
  Scenario: chain
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"

    * user inserts fragment "level1-first"
      | first name | last name |
      | Bob        | Burnquist |
    * user checks in the element "first name" value "Bob"
    * user checks in the element "last name" value "Burnquist"
    * user inserts fragment "level1-second"

