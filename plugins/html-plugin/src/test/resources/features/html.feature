#language = en
Feature: Check html yandex elements functional + ActionTitles mechanism

  @html
  Scenario: html elements check

    * user is on the page "MainY"
    * user in block "menu" finds button "Home"
    * user in block "menu" (go to page) with a parameter "Contact"
    * user is on the page "ContactY"

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

    * user selects the checkbox "checkbox"

    * user in block "menu" (go to page) with a parameter "Home"
    * user is on the page "MainY"