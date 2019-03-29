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

  @data=$Data @stash-and-data-outline
  Scenario Outline: Test Stash and data in scenario outline
    # For testing stash and non critical in not defined step
#    * ? user filsdsdsdsls the field "first name" with value "${NOT_FOUND}"
    * user fills the field "first name" with value "$Data{<role>.<field>} <prefix>"
    * user checks in the element "first name" value "Alex <prefix>"
    * user fills the field "first name" with value ""
    * user checks that the field "first name" is empty
    * user fills form
      | first name | ${<role and field>} <prefix> |
    * user checks in the element "first name" value "Alex <prefix>"
    * user fills the field "first name" with value ""
    * user checks that the field "first name" is empty
    * stores the value "Al" in a variable "PART"
    * user fills the field "first name"
    """
    #{PART}ex ${<role and field>} <prefix>
    """
    * user checks in the element "first name" value "Alex Alex <prefix>"
    * user fills the field "first name" with value ""
    * user checks that the field "first name" is empty
    * user fills the field "first name" with value "<full path>"
    * user checks in the element "first name" value "Alex"

    * user fills the field "first name" with value ""
    * user checks that the field "first name" is empty
    * user fills the field "first name" with value "${<role and field>}"
    * user checks in the element "first name" value "Alex"

    * user performs "fill fragment"
      | first name               | button name |
      | $Data{<role>.first name} | send        |
    * user fills the field "${<role>.id}" "Alex"

    Examples:
      | role  | field      | role and field   | full path           | prefix |
      | Admin | first name | Admin.first name | ${Admin.first name} | Plesk  |

  @stash-and-data @data=$Data{Admin}
  Scenario: Test Stash and data in scenario
    # For testing stash and non critical in not defined step
#    * ? user filsdsdsdsls the field "first name" with value "${NOT_FOUND}"
    * user fills the field "first name" with value "$Data{Admin.first name} Plesk"
    * user checks in the element "first name" value "Alex Plesk"
    * user fills the field "first name" with value ""
    * user checks that the field "first name" is empty
    * user fills form
      | first name | ${first name} Plesk |
    * user checks in the element "first name" value "Alex Plesk"
    * user fills the field "first name" with value ""
    * user checks that the field "first name" is empty
    * stores the value "Al" in a variable "PART"
    * user fills the field "first name"
    """
    #{PART}ex ${first name} Plesk
    """
    * user checks in the element "first name" value "Alex Alex Plesk"