#language: en
@data=$Data
Feature: Data sources

  Background:
    * ? user is on the page "Main"

  @test-non-critical
  Scenario: Data From Feature Tag
    * ? user clicks the button "Contact"
    * ? user is on the page "Contact"
    * ? failed step
    * user inserts fragment "fill non critical fragment"
      | first name          | button name |
      | ${Admin.first name} | send        |
    * ? user checks in the element "first name" value "Alex"

  @test-non-critical
  Scenario: Data From Feature Tag 2
    * ? user clicks the button "Contact"
    * ? user is on the page "Contact"
    * user inserts fragment "fill non critical fragment"
      | first name          | button name |
      | ${Admin.first name} | send        |
    * ? user checks in the element "first name" value "Alex"

  @testing @data=$Data{Admin}
  Scenario: Data From Scenario Tag
    * ? user clicks the button "Contact"
    * ? user is on the page "Contact"
    * ? failed step
    * user inserts fragment "fill non critical fragment"
      | first name    | button name |
      | ${first name} | send        |
    * user inserts fragment "fill non critical fragment"
      | first name    | button name |
      | ${first name} | send        |
    * user checks in the element "first name" value "Alex"

  @data=$Data{Admin}
  Scenario: Data From Fill Path
    * ? user clicks the button "Contact"
    * ? user is on the page "Contact"
    * ? failed step
    * user checks that the field "first name" is empty
    * user fills the field "first name" "$Data{Admin.last name}"
    * user checks that the field "first name" is not empty
    * ? user checks in the element "first name" value "Alex"
    * user checks in the element "first name" that the value is not equal "$Data{Operator.first name}"