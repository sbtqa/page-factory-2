#language: en
@test-data @data=$Data
Feature: Data sources

  Background:
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"

  Scenario: Data From Feature Tag
    * user inserts fragment "fill fragment"
      | first name          | button name |
      | ${Admin.first name} | send        |
    * user checks in the element "first name" value "Alex"

  @data=$Data{Admin}
  Scenario: Data From Scenario Tag
    * user inserts fragment "fill fragment"
      | first name    | button name |
      | ${first name} | send        |
    * user checks in the element "first name" value "Alex"

  @data=$Data{Admin}
  Scenario: Data From Fill Path
    * user checks that the field "first name" is empty
    * user fills the field "first name" "$Data{Admin.first name}"
    * user checks that the field "first name" is not empty
    * user checks in the element "first name" value "Alex"
    * user checks in the element "first name" that the value is not equal "$Data{Operator.first name}"

  @stash @data=$Data{Admin}
  Scenario: Common Stash for few scenarios 1
    * stores the value "$Data{Admin.first name}" in a variable "TEMP"

  @stash @data=$Data{Admin}
  Scenario: Common Stash for few scenarios 2
    * checks that the key "TEMP" value is empty

  @stash @data=$Data{Admin}
  Scenario: Data From Fill Path
    * stores the value "$Data{Admin.first name}" in a variable "TEMP"
    * user checks that the field "first name" is empty
    * user fills the field from stash "first name" "TEMP"
    * user checks that the field "first name" is not empty
    * user checks in the element "first name" value "Alex"
    * user checks in the element "first name" that the value is not equal "$Data{Operator.first name}"

  @stash @data=$Data{Admin}
  Scenario: Data From Fill Path
    * stores the value "$Data{Admin.first name}" in a variable "TEMP"
    * user checks that the field "first name" is empty
    * user fills the field from stash
      | first name | TEMP |
    * user fills the field from stash "first name"
    """
    TEMP
    """
    * user checks that the field "first name" is not empty
    * user checks in the element "first name" value "Alex"
    * user checks in the element "first name" that the value is not equal "$Data{Operator.first name}"

  @newstash @data=$Data{Admin}
  Scenario: Test Stash
    * stores the value "$Data{Admin.first name}" in a variable "TEMPLATE"
    * user checks that the field "first name" is empty
    * user fills the field "first name" with value "#{TEMPLATE}"
    * user checks in the element "first name" value "Alex"
    * user fills the field "first name" with value ""
    * user checks that the field "first name" is empty
    * user fills form
      | first name | #{TEMPLATE} |
    * user checks in the element "first name" value "Alex"
    * user fills the field "first name" with value ""
    * user checks that the field "first name" is empty
    * stores the value "Al" in a variable "PART"
    * user fills the field "first name"
    """
    #{PART}ex
    """
    * user checks that the field "first name" is not empty
    * user checks in the element "first name" value "Alex"

  @formatter @data=$Data{Admin}
  Scenario: Test formatter for stash
    * stores the value "$Data{Admin.first name}" in a variable "TEMPLATE"
    * stores the value "first name" in a variable "NAME"
    * stores the value "first name" in a variable "LONG FIRST NAME"
    * user checks that the field "#{NAME}" is empty
    * user fills the field "#{LONG FIRST NAME}" with value "#{TEMPLATE}"
    * user checks in the element "#{NAME}" value "Alex"
    * user fills the field "#{NAME}" with value ""
    * user fills the field "#{NAME}" or "#{LONG FIRST NAME}" with value "#{TEMPLATE}"
    * user fills the field "#{NAME}" or "first name" with value "#{TEMPLATE}"
    * user fills the field "#{NAME}" "#{LONG FIRST NAME}" "#{TEMPLATE}"
    * user fills form
      | #{NAME} | #{TEMPLATE} |