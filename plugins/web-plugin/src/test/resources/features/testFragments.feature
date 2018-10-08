#language: en
Feature: Check fragments functional

  @test-fragments
  Scenario Outline: Check fragments functional
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"

    #CHECKS
    * user inserts fragment "checks fragment"

    #ACTIONS
    * user (clears all of the fields)

    #click
    * user inserts fragment "click fragment"

    #fill
    * user inserts fragment "fill fragment"
      | first name | button name |
      | <name>     | send        |

    Examples:
      | name |
      | Tony |
      | Alex |