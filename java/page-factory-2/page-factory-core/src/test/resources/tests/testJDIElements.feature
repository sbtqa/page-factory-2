#language = en
Feature: Check jdi elements functional + ActionTitles mechanism

  @jdiElementsCheck
  Scenario: web elements check

    * user is on the page "MainJ"
    * user (click the button) "Contact"
    * user is on the page "ContactJ"

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
    * user (select dropdown value) "City" "Saint-Petersburg"

    * user (click the button) "Donations"

    * user is on the page "DonationsJ"
    * user (read table)

    * user (go to page) "Home"
    * user is on the page "Main"