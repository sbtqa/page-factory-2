#language = en
Feature: Check html yandex elements functional + ActionTitles mechanism

  @find
  Scenario: html elements check

    * user is on the page "MainY"
    * user clicks the button "menu->Home"
    * user in block "menu" (go to page) with a parameter "Contact"
    * user is on the page "ContactY"

    * user fills the field "input list" "Alex"
    * user fills the field "input list->3" "Alex"
    * user fills the field "input list->1" "Bro"
    * user fills the field "input list->5" "Bro"