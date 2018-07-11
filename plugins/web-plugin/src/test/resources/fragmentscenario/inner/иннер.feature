#language: en
Feature: Check web elements functional + ActionTitles mechanism

  @test
  Scenario: Check RedirectTo annotation in web elements
    * user (fill the field) "Contact2"
    * user (click the button) "Contact2"
    * user is on the page "Contact2"
