#language: en
@fragments
Feature: actions fragments

  @fragment
  Scenario: level1-first
    * user (fill the field) "first name" "<first name>"
    * user (checks value) "first name" "Bob"
    * user inserts fragment "level2-first"
      | last name   |
      | <last name> |

  @fragment
  Scenario: level1-second
    * user (fill the field) "first name" "<first name>"
    * user (checks value) "first name" "Steve"
    * user inserts fragment "level2-second"
      | last name   |
      | <last name> |
