#language: en
@dynamic @data=$DataFragments
Feature: Data sources

  Background:
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"

  Scenario: Relative path data
    * user inserts fragment "Relative ${unauthorized fragment name}"
      | first name          | button name |
      | ${Admin.first name} | send        |
    * user checks in the element "first name" value "${Admin.first name}"

  @data=$Fragments
  Scenario: Relative path data
    * user inserts fragment "${Fragment 1.name}"
      | first name                       | button name |
      | $DataFragments{Admin.first name} | send        |
    * user checks in the element "first name" value "$DataFragments{Admin.first name}"

  @data-fragment @data=$DataFragments{Admin}
  Scenario: recursive example of working with data in fragments
    * user inserts fragment "$Fragments{Fragment 5.name}"
      | first name    | button name |
      | ${first name} | send        |
    * user checks in the element "first name" value "${first name}"

  @data-fragment @data=$Fragments
  Scenario: recursive example of working with data in fragments
    * user inserts fragment "${Fragment 1.name}"
      | first name                       | button name |
      | $DataFragments{Admin.first name} | send        |
    * user checks in the element "first name" value "$DataFragments{Admin.first name}"
    * user inserts fragment "${Fragment 1.name}"
      | first name                          | button name |
      | $DataFragments{Operator.first name} | send        |
    * user checks in the element "first name" value "$DataFragments{Operator.first name}"

  @data-fragment @data=$Fragments
  Scenario: recursive example of working with data in fragments
    * user inserts fragment "${Fragment 1.name}"
      | first name                       | button name |
      | $DataFragments{Admin.first name} | send        |
    * user checks in the element "first name" value "$DataFragments{Admin.first name}"
    * user inserts fragment "$Fragments{Fragment 5.name}"
      | first name                          | button name |
      | $DataFragments{Operator.first name} | send        |
    * user checks in the element "first name" value "$DataFragments{Operator.first name}"

  @data-fragment @data=$Fragments
  Scenario: recursive example of working with data in fragments
    * user inserts fragment "${Fragment 1.name}"
      | first name                       | button name |
      | $DataFragments{Admin.first name} | send        |
    * user checks in the element "first name" value "$DataFragments{Admin.first name}"
    * user inserts fragment "${Fragment 5.name}"
      | first name                          | button name |
      | $DataFragments{Operator.first name} | send        |
    * user checks in the element "first name" value "$DataFragments{Operator.first name}"