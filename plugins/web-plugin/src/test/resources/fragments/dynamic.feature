#language: en
@fragments
Feature: dynamic fragments

  @fragment
  Scenario: Fragment with other fragment
    * user fills the field "first name" "<first name>"
    * user inserts fragment "${Fragment 2.name}"
      | button name   |
      | <button name> |

  @fragment
  Scenario: Fragment with other fragment 2
    * user inserts fragment "${Fragment 3.name}"
      | button name   |
      | <button name> |
    * user (check that error message not contains) "Please specify your first last name"

  @fragment
  Scenario: Fragment with other fragment 3
    * user fills the field "first name" "<first name>"
    * user inserts fragment "$Fragments{Fragment 6.name}"
      | button name   |
      | <button name> |

  @fragment
  Scenario: Fragment with other fragment 4
    * user clicks the button "<button name>"

  @fragment
  Scenario: Simple fragment
    * user clicks the button "<button name>"

  @fragment
  Scenario: Relative data path for fragments
    * user fills the field "first name" "<first name>"
    * user inserts fragment "${Admin.fragment name}"
      | button name   |
      | <button name> |
    * user (check that error message not contains) "Please specify your first last name"