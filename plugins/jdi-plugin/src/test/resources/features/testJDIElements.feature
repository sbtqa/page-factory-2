#language = en
Feature: Check jdi elements functional + ActionTitles mechanism

  @jdiElementsCheck
  Scenario: web elements check

    * user is on the page "MainJ"
    * user clicks the button "Contact"
    * user is on the page "ContactJ"

    * user (check that error message not contains) "Please specify your first name"
    * user clicks the button "send"
    * user (check that error message contains) "Please specify your first name"

    * user fills the field "first name" "Alex"
    * user clicks the button "send"
    * user (check that error message not contains) "Please specify your first name"

    * user (check that error message contains) "Please specify your last name"
    * user clicks the button "send"
    * user (check that error message contains) "Please specify your last name"

    * user fills the field "last name" "Alexeev"
    * user clicks the button "send"
    * user (check that error message not contains) "Please specify your last name"