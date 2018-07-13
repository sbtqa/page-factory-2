#language: en
Feature: Check web elements functional + ActionTitles mechanism

  @test-fragments
  Scenario: Check RedirectTo annotation in web elements
    * user is on the page "Main"
    * user (click the button) "Contact"
    * user is on the page "Contact"

    #CHECKS
    * user (insert fragment) "checks fragment"

    #ACTIONS
    * user (clears all of the fields)
  
    #click
    * user (insert fragment) "click fragment"
  
    #fill
    * user (insert fragment) "fill fragment"
      | first name  | Allex   |
      | button name | send    |