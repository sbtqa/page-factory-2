#language: en
Feature: Check web elements functional + ActionTitles mechanism

  @test
  Scenario: Check RedirectTo annotation in web elements
    * user (fill the field) "Contact1"
    * user (click the button) "Contact1"
    * user is on the page "Contact1"