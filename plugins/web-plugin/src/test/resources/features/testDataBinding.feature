#language: en
@test-data @data=$Data
Feature: Data sources

  Background:
    * user is on the page "Main"
    * user (click the button) "Contact"
    * user is on the page "Contact"

  Scenario: Data From Feature Tag
    * user (insert fragment) "fill_fragment"
      | first name          | button name |
      | ${Admin.first name} | send        |

  @data=$Data{Admin}
  Scenario: Data From Scenario Tag
    * user (insert fragment) "fill_fragment"
      | first name    | button name |
      | ${first name} | send        |


  @data=$Data{Admin}
  Scenario: Data From Fill Path
    * user (checks that the field is empty) "first name"
    * user (fill the field) "first name" "$Data{Admin.first name}"
    * user (checks that the field is not empty) "first name"
    * user (checks value) "first name" "${first name}"
    * user (check that values are not equal) "first name" "$Data{Operator.first name}"