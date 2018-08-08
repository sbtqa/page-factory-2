#language: en
@fragments
Feature: actions fragments

  @fragment
  Scenario: level2-first
    * user (fill the field) "last name" "<last name>"
    * user (checks value) "last name" "Burnquist"
    * user inserts fragment "level3"

  @fragment
  Scenario: level2-second
    * user (fill the field) "last name" "<last name>"
    * user (checks value) "last name" "Caballero"
    * user inserts fragment "level3"