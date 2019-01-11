#language = en
Feature: Check find html elements

Background:
  * user is on the page "MainY"

  @find-simple
  Scenario: Search simple elements with different types
    * user clicks the button "menu->Home"
    * user in block "menu" (go to page) with a parameter "Contact"
    * user is on the page "ContactY"
    * (check find web element)
    * (check find block)
    * (check find typified element)
    * (check find element with incorrect type)

  @find-list
  Scenario: Search list element in different combinations
    * user clicks the button "menu with list elements->toolbar->2"
    * user is on the page "ContactY"
    * user fills the field "input list" "Alex"
    * user fills the field "input list->3" "Alex"
    * user fills the field "input list->1" "Bro"
    * user fills the field "input list->5" "Bro"

@find-path
  Scenario: Checks the path assembly
    * user clicks the button "menu with list elements->toolbar->3"
    * (opened page) "Donations"
    * user clicks the button "menu with list elements->toolbar->1"
    * (opened page) "Home"
