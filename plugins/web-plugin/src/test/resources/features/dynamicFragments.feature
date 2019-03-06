#language: en
@dynamic @data=$DataFragments
Feature: Data sources

  Background:
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"

  Scenario: Relative path data
    * user inserts fragment "${unauthorized fragment name}"
      | first name    | button name |
      | ${Admin.first name} | send        |
    * user checks in the element "first name" value "Alex" 
 
  @data-fragment @data=$DataFragments{Admin}
  Scenario: recursive example of working with data in fragments
    * user inserts fragment "$Fragments{Fragment 1.name}"
      | first name    | button name |
      | ${first name} | send        |
    * user checks in the element "first name" value "Alex"