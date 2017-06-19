#language = en
  Feature: Check web elements functional + ActionTitles mechanism

    @webElementsCheck
    Scenario: web elements check

      * user is on the page "Main"
      * user (click the button) "Contact"
      * user is on the page "Contact"

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

      * user (go to page) "Home"
      * user is on the page "Main"