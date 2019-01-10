#language: en
Feature: background fragment

  @fragment
  Scenario: background level 2
    * user inserts fragment "background level 3"
    * user inserts fragment "background level 4"
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"

  @fragment
  Scenario: background level 3
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"

  @fragment
  Scenario: background level 4
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"

  @fragment
  Scenario: go to contact page
    * user inserts fragment "background level 2"
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"


