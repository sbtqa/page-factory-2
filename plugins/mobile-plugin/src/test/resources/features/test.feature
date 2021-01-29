#language: en
Feature: Calc testing

  @test
  Scenario: Calc testing
    * user is on the page "Calc"
    * user clicks the button "1"
    * user clicks the button "+"
    * user clicks the button "2"
#    * user clicks the button "="
    * user clicks the button "="
    * user checks in the element "result" value "3"