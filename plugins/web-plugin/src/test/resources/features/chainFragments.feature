#language: en
Feature: Check fragments functional

  @chain
  Scenario: chain
    * user is on the page "Main"
    * user (click the button) "Contact"
    * user is on the page "Contact"

    * user inserts fragment "level1-first"
      | first name | last name |
      | Bob        | Burnquist |
    * user inserts fragment "level1-second"
      | first name | last name |
      | Steve      | Caballero |

