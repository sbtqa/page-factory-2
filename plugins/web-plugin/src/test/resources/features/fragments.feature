#language: en
@fragments
Feature: Fragments

  Background:
    * user inserts fragment "go to contact page"

  @test-fragments
  Scenario Outline: Check fragments functional

    #CHECKS
    * user performs "checks fragment"

    #ACTIONS
    * user (clears all of the fields)

    #click
    * user performs "click fragment" scenario

    #fill
    * user inserts fragment "fill fragment"
      | first name | button name |
      | <name>     | send        |

    Examples:
      | name |
      | Tony |
      | Alex |

  @test-fragments-datatable
  Scenario: Check fragments datatable functional
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"

    * user inserts fragment "fill fragment"
      | first name | button name |
      | Alex       | send        |
      | Tony       | send        |
    * user checks in the element "first name" value "Tony"