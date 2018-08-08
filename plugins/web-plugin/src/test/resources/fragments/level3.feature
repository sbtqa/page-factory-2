#language: en
@fragments
Feature: actions fragments

  @fragment
  Scenario: level3
    * user (fill the field) "first name" "Eric"
    * user (checks value) "first name" "Eric"
    * user (fill the field) "last name" "Koston"
    * user (checks value) "last name" "Koston"
