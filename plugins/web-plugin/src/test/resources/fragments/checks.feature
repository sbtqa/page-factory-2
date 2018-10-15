#language: en
@fragments
Feature: checks fragments

  @fragment
  Scenario: checks fragment
    * user checks that the field "first name" is empty
    * user fills the field "first name" "Alex"
    * user checks that the field "first name" is not empty
    * user checks in the element "first name" value "Alex"
    * user checks in the element "first name" that the value is not equal "Billy"