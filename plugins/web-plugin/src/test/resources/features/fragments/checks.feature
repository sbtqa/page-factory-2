#language: en
@fragments
Feature: checks fragments

  @fragment=checks_fragment
  Scenario: checks fragment
    * user (checks that the field is empty) "first name"
    * user (fill the field) "first name" "Alex"
    * user (checks that the field is not empty) "first name"
    * user (checks value) "first name" "Alex"
    * user (check that values are not equal) "first name" "Billy"