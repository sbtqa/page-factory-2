#language: en
Feature: Check web elements functional + ActionTitles mechanism

  @web
  Scenario: Check web elements functional + ActionTitles mechanism
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"

    #CHECKS
    * user checks that the field "first name" is empty
    * user fills the field "first name" with value "Alex"
    * user checks that the field "first name" is not empty
    * user checks in the element "first name" value "Alex"
    * user checks in the element "first name" that the value is not equal "Billy"

    #ACTIONS
    * user (clears all of the fields)

    #click
    * user clicks the button "send"
    * user (check that error message contains) "Please specify your first name"

    #fill
    * user fills the field "first name" with value "Alex"
    * user clicks the button "send"
    * user (check that error message not contains) "Please specify your first name"

    #press
    * user (clears all of the fields)
    * user presses the key "Enter" on the element "send"
    * user (check that error message contains) "Please specify your first name"
    * user fills the field "first name" with value "Alex"
    * user presses the key "Enter" on the element "send"
    * user (check that error message not contains) "Please specify your first name"

    #select
    * user selects in "state" the value "Novosibirsk"
    * user checks in the element "state" value "Novosibirsk"

    #checkbox
    * user selects the checkbox "checkbox"
    * user (checks checkbox) "true"

    #alert
    * user clicks the button "alert"
    * user accepts alert with text "Alert text"
    * user clicks the button "alert"
    * user dismisses alert with text "Alert text"