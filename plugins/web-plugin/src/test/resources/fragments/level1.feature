#language: en
@fragments
Feature: actions fragments

  @fragment
  Scenario: level1-first
    * user fills the field "first name" "<first name>"
    * user inserts fragment "level2-first"
      | last name   |
      | <last name> |

  @fragment
  Scenario: level1-second
    * user fills the field "first name" "Eric"
    * user checks in the element "first name" value "Eric"
    * user fills the field "last name" "Koston"
    * user checks in the element "last name" value "Koston"
