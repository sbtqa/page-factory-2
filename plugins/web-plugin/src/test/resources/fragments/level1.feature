#language: en
@fragments
Feature: actions fragments

  @fragment
  Scenario: level1-first
    * user (fill the field) "first name" "<first name>"
    * user inserts fragment "level2-first"
      | last name   |
      | <last name> |

  @fragment
  Scenario: level1-second
    * user (fill the field) "first name" "Eric"
    * user (checks value) "first name" "Eric"
    * user (fill the field) "last name" "Koston"
    * user (checks value) "last name" "Koston"
