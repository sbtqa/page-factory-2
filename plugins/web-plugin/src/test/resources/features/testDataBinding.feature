#language: en
@test-data @data=$Data
Feature: Data sources

  Background:
    * user is on the page "Main"
    * user (click the button) "Contact"
    * user is on the page "Contact"

  Scenario: Data From Feature Tag
    * user inserts fragment "fill fragment"
      | first name          | button name |
      | ${Admin.first name} | send        |
    * user (checks value) "first name" "Alex"

  @data=$Data{Admin}
  Scenario: Data From Scenario Tag
    * user inserts fragment "fill fragment"
      | first name    | button name |
      | ${first name} | send        |
    * user (checks value) "first name" "Alex"

  @data=$Data{Admin}
  Scenario: Data From Fill Path
    * user (checks that the field is empty) "first name"
    * user (fill the field) "first name" "$Data{Admin.first name}"
    * user (checks that the field is not empty) "first name"
    * user (checks value) "first name" "Alex"
    * user (check that values are not equal) "first name" "$Data{Operator.first name}"