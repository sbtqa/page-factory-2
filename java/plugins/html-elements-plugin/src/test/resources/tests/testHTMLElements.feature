#language = en
Feature: Check html yandex elements functional + ActionTitles mechanism

  @htmlElementsCheck
  Scenario: html elements check

    * user is on the page "MainY"
    * user in block "menu" (go to page) with a parameter "Contact"
    * user is on the page "ContactY"

    * user (check that error message not contains) "Please specify your first name"
    * user (click the button) "send"
    * user (check that error message contains) "Please specify your first name"

    * user (fill the field) "first name" "Alex"
    * user (click the button) "send"
    * user (check that error message not contains) "Please specify your first name"


    * user (check that error message contains) "Please specify your last name"
    * user (click the button) "send"
    * user (check that error message contains) "Please specify your last name"

    * user (fill the field) "last name" "Alexeev"
    * user (click the button) "send"
    * user (check that error message not contains) "Please specify your last name"

    * user in block "menu" (go to page) with a parameter "Home"
    * user is on the page "Main"