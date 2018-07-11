#language: en
Feature: Check web elements functional + ActionTitles mechanism

  @test
  Scenario: Check RedirectTo annotation in web elements
    * user is on the page "Main"
#    * user (fill the field) "Contact"
    * user (click the button) "Contact"
    * user is on the page "Contact"

    #CHECKS
    * user (insert fragment) "identification"

#    #ACTIONS
#    * user (clears all of the fields)
#
#    #click
#    * user (click the button) "send"
#    * user (check that error message contains) "Please specify your first name"
#
#    #fill
#    * user (fill the field) "first name" "Alex"
#    * user (click the button) "send"
#    * user (check that error message not contains) "Please specify your first name"
#
#    #press
#    * user (clears all of the fields)
#    * user (press the key) "Enter"
#    * user (check that error message contains) "Please specify your first name"
#    * user (fill the field) "first name" "Alex"
#    * user (press the key) "Enter"
#    * user (check that error message not contains) "Please specify your first name"
#
#    #select
#    * user (select) "state" "Novosibirsk"
#    * user (checks value) "state" "Novosibirsk"
#
#    #checkbox
#    * user (select checkbox) "checkbox"
#    * user (checks checkbox) "true"
#
#    #alert
#    * user (click the button) "alert"
#    * user (accepts alert) "Alert text"
#    * user (click the button) "alert"
#    * user (dismisses alert) "Alert text"
