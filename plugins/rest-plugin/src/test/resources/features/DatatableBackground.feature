#language:en
@dаtatable-background
Feature: Datatable test

  Background:
    * user sends request for "api request with params test" with parameters
      | query-parameter-name-1  | query-parameter-value-1  |
      | header-parameter-name-1 | #{header} |


  @test-stash-1
  Scenario: test1
    * system returns "result with datatable" with parameters
      | query-parameter-name-1  | query-parameter-value-1  |
      | header-parameter-name-1 | header-parameter-value-1 |

  @test-stash-2
  Scenario: test2
    * system returns "result with datatable" with parameters
      | query-parameter-name-1  | query-parameter-value-1  |
      | header-parameter-name-1 | header-parameter-value-1 |


