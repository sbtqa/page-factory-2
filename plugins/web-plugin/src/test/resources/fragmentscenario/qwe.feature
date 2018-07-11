#language: en
Feature: Check web elements functional + ActionTitles mechanism

  @test
  Scenario: Check RedirectTo annotation in web elements
    * user (fill the field) "Contact"
    * user (click the button) "Contact"
    * user is on the page "Contact"


